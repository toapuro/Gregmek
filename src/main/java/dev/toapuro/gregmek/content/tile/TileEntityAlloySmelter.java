package dev.toapuro.gregmek.content.tile;

import dev.toapuro.gregmek.content.recipe.AlloySmeltingRecipe;
import dev.toapuro.gregmek.content.recipe.cache.SingleShapelessInputsCachedRecipe;
import dev.toapuro.gregmek.content.recipe.lookup.ISingleMultipleRecipeLookupHandler;
import dev.toapuro.gregmek.content.recipe.lookup.cache.GMInputRecipeCache;
import dev.toapuro.gregmek.content.registry.GMBlocks;
import dev.toapuro.gregmek.content.registry.recipe.GMRecipeType;
import dev.toapuro.gregmek.content.tile.component.TileComponentCircuit;
import dev.toapuro.gregmek.content.tile.interfaces.ITileProgrammable;
import mekanism.api.IContentsListener;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TileEntityAlloySmelter extends TileEntityProgressMachine<AlloySmeltingRecipe> implements ISingleMultipleRecipeLookupHandler.ItemsMultipleRecipeLookupHandler<AlloySmeltingRecipe>, ITileProgrammable {
    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES;

    static {
        TRACKED_ERROR_TYPES = List.of(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
    }

    private final TileComponentCircuit circuitComponent;
    private final IOutputHandler<@NotNull ItemStack> outputHandler;
    private final IInputHandler<@NotNull ItemStack> inputHandler;
    private final IInputHandler<@NotNull ItemStack> secondaryInputHandler;
    @WrappingComputerMethod(
            wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class,
            methodNames = {"getMainInput"},
            docPlaceholder = "main input slot"
    )
    InputInventorySlot mainInputSlot;
    @WrappingComputerMethod(
            wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class,
            methodNames = {"getSecondaryInput"},
            docPlaceholder = "secondary input slot"
    )
    InputInventorySlot secondaryInputSlot;
    @WrappingComputerMethod(
            wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class,
            methodNames = {"getOutput"},
            docPlaceholder = "output slot"
    )
    OutputInventorySlot outputSlot;
    @WrappingComputerMethod(
            wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class,
            methodNames = {"getEnergyItem"},
            docPlaceholder = "energy slot"
    )
    EnergyInventorySlot energySlot;
    private MachineEnergyContainer<TileEntityAlloySmelter> energyContainer;

    public TileEntityAlloySmelter(BlockPos pos, BlockState state) {
        super(GMBlocks.ALLOY_SMELTER, pos, state, TRACKED_ERROR_TYPES, 200);
        this.configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY);
        this.configComponent.setupItemIOConfig(List.of(this.mainInputSlot, this.secondaryInputSlot), Collections.singletonList(this.outputSlot), energySlot, false);
        this.configComponent.setupInputConfig(TransmissionType.ENERGY, this.energyContainer);
        this.ejectorComponent = new TileComponentEjector(this);
        this.ejectorComponent.setOutputData(this.configComponent, TransmissionType.ITEM);
        this.inputHandler = InputHelper.getInputHandler(this.mainInputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
        this.secondaryInputHandler = InputHelper.getInputHandler(this.secondaryInputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        this.outputHandler = OutputHelper.getOutputHandler(this.outputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE);

        this.circuitComponent = new TileComponentCircuit(this);
    }

    protected @NotNull IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(this.energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    protected @NotNull IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(this.mainInputSlot = InputInventorySlot.at(this::containsRecipe, this::containsRecipe, recipeCacheListener, 25, 35))
                .tracksWarnings((slot) -> slot
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, this.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(this.secondaryInputSlot = InputInventorySlot.at(this::containsRecipe, this::containsRecipe, recipeCacheListener, 43, 35))
                .tracksWarnings((slot) -> slot
                        .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, this.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT)));
        builder.addSlot(this.outputSlot = OutputInventorySlot.at(listener, 124, 35))
                .tracksWarnings((slot) -> slot
                        .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, this.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));
        builder.addSlot(this.energySlot = EnergyInventorySlot.fillOrConvert(this.energyContainer, this::getLevel, listener, 124, 56));
        return builder.build();
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        this.energySlot.fillContainerOrConvert();
        this.recipeCacheLookupMonitor.updateAndProcess();
    }

    public @NotNull IMekanismRecipeTypeProvider<AlloySmeltingRecipe, GMInputRecipeCache.Items<AlloySmeltingRecipe>> getRecipeType() {
        return GMRecipeType.ALLOY_SMELTING;
    }

    public @Nullable AlloySmeltingRecipe getRecipe(int cacheIndex) {
        return this.findFirstRecipeHandlers(List.of(this.inputHandler, this.secondaryInputHandler));
    }

    public @NotNull CachedRecipe<AlloySmeltingRecipe> createNewCachedRecipe(@NotNull AlloySmeltingRecipe recipe, int cacheIndex) {
        CachedRecipe<AlloySmeltingRecipe> cachedRecipe =
                SingleShapelessInputsCachedRecipe.alloySmelter(recipe, this.recheckAllRecipeErrors, this.inputHandler, this.secondaryInputHandler, this.outputHandler).setErrorsChanged((x$0) -> this.onErrorsChanged(x$0)).setCanHolderFunction(() -> MekanismUtils.canFunction(this)).setActive(this::setActive);
        MachineEnergyContainer<TileEntityAlloySmelter> energyContainer = this.energyContainer;
        Objects.requireNonNull(energyContainer);
        return cachedRecipe.setEnergyRequirements(energyContainer::getEnergyPerTick, this.energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    public MachineEnergyContainer<TileEntityAlloySmelter> getEnergyContainer() {
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
    public TileComponentCircuit getCircuitComponent() {
        return this.circuitComponent;
    }
}
