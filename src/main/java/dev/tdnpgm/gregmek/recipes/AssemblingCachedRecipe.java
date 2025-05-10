package dev.tdnpgm.gregmek.recipes;

import dev.tdnpgm.gregmek.utils.GregmekUtils;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public class AssemblingCachedRecipe extends CachedRecipe<AssemblingRecipe> {
    private final IOutputHandler<@NotNull ItemStack> outputHandler;
    private final List<IInputHandler<@NotNull ItemStack>> itemInputHandlers;
    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;
    private final List<ItemStack> recipeItems;
    private FluidStack recipeFluid;

    @Nullable
    private AssemblingRecipe.AssemblingRecipeOutput output;

    public AssemblingCachedRecipe(AssemblingRecipe recipe, BooleanSupplier recheckAllErrors, List<IInputHandler<@NotNull ItemStack>> itemInputHandlers, IInputHandler<@NotNull FluidStack> fluidInputHandler, IOutputHandler<@NotNull ItemStack> outputHandler) {
        super(recipe, recheckAllErrors);
        this.recipeItems = GregmekUtils.generateNList(() -> ItemStack.EMPTY, AssemblingRecipe.itemSlots);
        this.recipeFluid = FluidStack.EMPTY;
        this.itemInputHandlers = Objects.requireNonNull(itemInputHandlers, "Item input handler cannot be null.");
        this.fluidInputHandler = Objects.requireNonNull(fluidInputHandler, "Fluid input handler cannot be null.");
        this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
    }

    protected void calculateOperationsThisTick(CachedRecipe.@NotNull OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            for (int i = 0; i < recipeItems.size(); i++) {
                ItemStack recipeItem = recipeItems.get(i);
                IInputHandler<@NotNull ItemStack> itemInputHandler = itemInputHandlers.get(i);

                recipeItems.set(i, itemInputHandler.getRecipeInput(recipe.getInputSolids().get(i)));
                if (recipeItem.isEmpty()) {
                    tracker.mismatchedRecipe();
                } else {
                    this.recipeFluid = this.fluidInputHandler.getRecipeInput(this.recipe.getInputFluid());
                    if (this.recipeFluid.isEmpty()) {
                        tracker.mismatchedRecipe();
                    } else {
                        itemInputHandler.calculateOperationsCanSupport(tracker, recipeItem);
                        if (tracker.shouldContinueChecking()) {
                            this.fluidInputHandler.calculateOperationsCanSupport(tracker, this.recipeFluid);
                            if (tracker.shouldContinueChecking()) {
                                if (tracker.shouldContinueChecking()) {
                                    this.output = this.recipe.getOutput(this.recipeItems, this.recipeFluid);
                                    this.outputHandler.calculateOperationsCanSupport(tracker, this.output.item());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isInputValid(IInputHandler<@NotNull ItemStack> itemInputHandler) {
        ItemStack item = itemInputHandler.getInput();

        if (item.isEmpty()) {
            return false;
        } else {
            FluidStack fluid = (FluidStack)this.fluidInputHandler.getInput();
            return !fluid.isEmpty() &&
                    this.recipe.test(itemInputHandlers.stream().map(IInputHandler::getInput).toList(), Collections.singletonList(fluid));
        }
    }

    public boolean isInputValid() {
        return itemInputHandlers.stream().allMatch(this::isInputValid);
    }

    protected void finishProcessing(int operations) {
        for (int i = 0; i < recipeItems.size(); i++) {
            ItemStack recipeItem = recipeItems.get(i);
            IInputHandler<@NotNull ItemStack> itemInputHandler = itemInputHandlers.get(i);

            if (this.output != null && !recipeItem.isEmpty() && !this.recipeFluid.isEmpty()) {
                itemInputHandler.use(recipeItem, operations);
                this.fluidInputHandler.use(this.recipeFluid, operations);
                this.outputHandler.handleOutput(this.output.item(), operations);
            }
        }
    }
}
