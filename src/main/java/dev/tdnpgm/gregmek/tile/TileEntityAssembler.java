package dev.tdnpgm.gregmek.tile;

import dev.tdnpgm.gregmek.recipes.AssemblingRecipe;
import dev.tdnpgm.gregmek.recipes.caches.AssemblingCachedRecipe;
import dev.tdnpgm.gregmek.recipes.lookup.IDoubleMultipleRecipeLookupHandler;
import dev.tdnpgm.gregmek.recipes.lookup.cache.GregmekInputRecipeCache;
import dev.tdnpgm.gregmek.registry.GMBlocks;
import dev.tdnpgm.gregmek.registry.recipe.GMRecipeType;
import dev.tdnpgm.gregmek.tile.component.TileComponentCircuit;
import dev.tdnpgm.gregmek.tile.interfaces.ITileProgrammable;
import dev.tdnpgm.gregmek.utils.GregmekUtils;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TileEntityAssembler extends TileEntityProgressMachine<AssemblingRecipe> implements IDoubleMultipleRecipeLookupHandler.ItemsFluidsMultipleRecipeLookupHandler<AssemblingRecipe>, ITileProgrammable {
    public static final RecipeError NOT_ENOUGH_ITEM_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_FLUID_INPUT_ERROR = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR = RecipeError.create();
    private static final List<RecipeError> TRACKED_ERROR_TYPES;

    private final TileComponentCircuit circuitComponent;
    private final IOutputHandler<@NotNull ItemStack> outputHandler;
    private final List<IInputHandler<@NotNull ItemStack>> itemInputHandlers;
    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;
    private MachineEnergyContainer<TileEntityAssembler> energyContainer;


    @WrappingComputerMethod(
            wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class,
            methodNames = {"getInputs"},
            docPlaceholder = "input slots"
    )
    List<InputInventorySlot> inputSlots;
    @WrappingComputerMethod(
            wrapper = SpecialComputerMethodWrapper.ComputerFluidTankWrapper.class,
            methodNames = {"getInputFluid", "getInputFluidCapacity", "getInputFluidNeeded", "getInputFluidFilledPercentage"},
            docPlaceholder = "fluid input"
    )
    public BasicFluidTank inputFluidTank;
    @WrappingComputerMethod(
            wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class,
            methodNames = {"getSecondaryInput"},
            docPlaceholder = "secondary input slot"
    )
    OutputInventorySlot outputSlot;
    @WrappingComputerMethod(
            wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class,
            methodNames = {"getEnergyItem"},
            docPlaceholder = "energy slot"
    )
    EnergyInventorySlot energySlot;

    public TileEntityAssembler(BlockPos pos, BlockState state) {
        super(GMBlocks.ASSEMBLING_MACHINE, pos, state, TRACKED_ERROR_TYPES, 200);
        this.configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID, TransmissionType.ENERGY);
        this.configComponent.setupItemIOConfig(
                GregmekUtils.collectOfType(IInventorySlot.class, inputSlots),
                Collections.singletonList(outputSlot), energySlot, false);
        this.configComponent.setupInputConfig(TransmissionType.FLUID, this.inputFluidTank);
        this.configComponent.setupInputConfig(TransmissionType.ENERGY, this.energyContainer);
        this.ejectorComponent = new TileComponentEjector(this);
        this.ejectorComponent.setOutputData(this.configComponent, TransmissionType.ITEM);
        this.itemInputHandlers = inputSlots.stream()
                .map(inputInventorySlot ->
                        InputHelper.getInputHandler(inputInventorySlot, RecipeError.NOT_ENOUGH_INPUT))
                .toList();
        this.fluidInputHandler = InputHelper.getInputHandler(this.inputFluidTank, NOT_ENOUGH_FLUID_INPUT_ERROR);
        this.outputHandler = OutputHelper.getOutputHandler(this.outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);

        this.circuitComponent = new TileComponentCircuit(this);
    }

    protected @NotNull IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(this.inputFluidTank = BasicFluidTank.input(10000, (fluid) ->
                this.containsRecipeBA(this.inputSlots.stream().map(BasicInventorySlot::getStack).toList(), Collections.singletonList(fluid)), recipeCacheListener));
        return builder.build();
    }

    protected @NotNull IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(this.energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    protected @NotNull IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        this.inputSlots = new ArrayList<>();

        for (int i = 0; i < AssemblingRecipe.MAX_ITEM_SLOTS; i++) {
            int slotXIndex = i % 3;
            int slotYIndex = i / 3;

            InputInventorySlot slot = InputInventorySlot.at(
                    this::containsRecipeA,
                    (stack) -> containsRecipeAB(Collections.singletonList(stack), Collections.singletonList(inputFluidTank.getFluid())), recipeCacheListener,
                        15+18* (slotXIndex), 17+18*slotYIndex);
            this.inputSlots.add(slot);
            builder.addSlot(slot)
                    .tracksWarnings((slotWarning) ->
                            slotWarning.warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, this.getWarningCheck(RecipeError.NOT_ENOUGH_INPUT)));
        }



        builder.addSlot(this.outputSlot = OutputInventorySlot.at(listener, 130, 35))
                .tracksWarnings((slot) ->
                        slot.warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, this.getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));

        builder.addSlot(this.energySlot = EnergyInventorySlot.fillOrConvert(this.energyContainer, this::getLevel, listener, 130, 56));
        return builder.build();
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        this.energySlot.fillContainerOrConvert();
        this.recipeCacheLookupMonitor.updateAndProcess();
    }

    public @NotNull IMekanismRecipeTypeProvider<AssemblingRecipe, GregmekInputRecipeCache.ItemsFluids<AssemblingRecipe>> getRecipeType() {
        return GMRecipeType.ASSEMBLING;
    }

    public @Nullable AssemblingRecipe getRecipe(int cacheIndex) {
        return this.findFirstRecipeHandlers(itemInputHandlers, Collections.singletonList(fluidInputHandler));
    }

    public @NotNull CachedRecipe<AssemblingRecipe> createNewCachedRecipe(@NotNull AssemblingRecipe recipe, int cacheIndex) {
        Objects.requireNonNull(this.energyContainer);
        return new AssemblingCachedRecipe(recipe, this.recheckAllRecipeErrors, this.itemInputHandlers, this.fluidInputHandler, this.outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(this.energyContainer::getEnergyPerTick, this.energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    public MachineEnergyContainer<TileEntityAssembler> getEnergyContainer() {
        return this.energyContainer;
    }

    public boolean isConfigurationDataCompatible(BlockEntityType<?> tileType) {
        return super.isConfigurationDataCompatible(tileType) || MekanismUtils.isSameTypeFactory(this.getBlockType(), tileType);
    }

    @ComputerMethod(
            methodDescription = "Get the energy used in the last tick by the machine"
    )
    FloatingLong getEnergyUsage() {
        return this.getActive() ? this.energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }

    @Override
    public void onCachedRecipeChanged(@Nullable CachedRecipe<AssemblingRecipe> cachedRecipe, int cacheIndex) {
        super.onCachedRecipeChanged(cachedRecipe, cacheIndex);

        int recipeDuration;
        if (cachedRecipe == null) {
            recipeDuration = 100;
        } else {
            AssemblingRecipe recipe = cachedRecipe.getRecipe();
            recipeDuration = recipe.getDuration();
        }

        boolean update = this.baseTicksRequired != recipeDuration;
        this.baseTicksRequired = recipeDuration;
        if (update) {
            this.recalculateUpgrades(Upgrade.SPEED);
        }
    }

    static {
        TRACKED_ERROR_TYPES = List.of(RecipeError.NOT_ENOUGH_ENERGY, NOT_ENOUGH_ITEM_INPUT_ERROR, NOT_ENOUGH_FLUID_INPUT_ERROR, NOT_ENOUGH_SPACE_ITEM_OUTPUT_ERROR, RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
    }

    @Override
    public TileComponentCircuit getCircuitComponent() {
        return this.circuitComponent;
    }
}
