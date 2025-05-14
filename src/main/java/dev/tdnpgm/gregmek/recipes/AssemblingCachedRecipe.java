package dev.tdnpgm.gregmek.recipes;

import dev.tdnpgm.gregmek.utils.GregmekUtils;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BooleanSupplier;

public class AssemblingCachedRecipe extends CachedRecipe<AssemblingRecipe> {
    private final IOutputHandler<@NotNull ItemStack> outputHandler;
    private final List<IInputHandler<@NotNull ItemStack>> itemInputHandlers;
    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;
    private final List<ItemStack> recipeItems;
    private final List<FluidStack> recipeFluids;

    @Nullable
    private ItemStack output;

    public AssemblingCachedRecipe(AssemblingRecipe recipe, BooleanSupplier recheckAllErrors, List<IInputHandler<@NotNull ItemStack>> itemInputHandlers, IInputHandler<@NotNull FluidStack> fluidInputHandler, IOutputHandler<@NotNull ItemStack> outputHandler) {
        super(recipe, recheckAllErrors);
        this.recipeItems = GregmekUtils.generateNList(() -> ItemStack.EMPTY, AssemblingRecipe.MAX_ITEM_SLOTS);
        this.recipeFluids = GregmekUtils.generateNList(() -> FluidStack.EMPTY, AssemblingRecipe.MAX_FLUID_SLOTS);
        this.itemInputHandlers = Objects.requireNonNull(itemInputHandlers, "Item input handler cannot be null.");
        this.fluidInputHandler = Objects.requireNonNull(fluidInputHandler, "Fluid input handler cannot be null.");
        this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
    }

    protected void calculateOperationsThisTick(CachedRecipe.@NotNull OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            ArrayList<ItemStackIngredient> itemSolidsQueue = new ArrayList<>(this.recipe.getInputSolids());

            for (int i = 0; i < this.itemInputHandlers.size(); i++) {
                IInputHandler<@NotNull ItemStack> itemInputHandler = this.itemInputHandlers.get(i);
                this.recipeItems.set(i, ItemStack.EMPTY);
                Iterator<ItemStackIngredient> queueIterator = itemSolidsQueue.iterator();
                while (queueIterator.hasNext()) {
                    ItemStackIngredient itemSolid = queueIterator.next();
                    ItemStack recipeInput = itemInputHandler.getRecipeInput(itemSolid);
                    if (!recipeInput.isEmpty()) {
                        this.recipeItems.set(i, recipeInput);
                        queueIterator.remove();
                    }
                }
            }

            List<FluidStackIngredient> inputFluids = this.recipe.getInputFluids();

            this.recipeFluids.replaceAll(fluidStack -> FluidStack.EMPTY);
            for (int i = 0; i < inputFluids.size(); i++) {
                this.recipeFluids.set(i, fluidInputHandler.getRecipeInput(inputFluids.get(i)));
            }

            if (!itemSolidsQueue.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
//                for (int i = 0; i < itemInputHandlers.size(); i++) {
//                    IInputHandler<@NotNull ItemStack> itemInputHandler = itemInputHandlers.get(i);
//                    ItemStack itemStack = recipeItems.get(i);
//                    itemInputHandler.calculateOperationsCanSupport(tracker, itemStack);
//                }

                this.output = this.recipe.getOutput();
                this.outputHandler.calculateOperationsCanSupport(tracker, this.output);
            }
        }
    }

    public boolean isInputValid(IInputHandler<@NotNull ItemStack> itemInputHandler) {
        ItemStack item = itemInputHandler.getInput();

        if (item.isEmpty()) {
            return true;
        } else {
            FluidStack fluid = this.fluidInputHandler.getInput();
            return this.recipe.test(itemInputHandlers.stream().map(IInputHandler::getInput).toList(), Collections.singletonList(fluid));
        }
    }

    public boolean isInputValid() {
        return itemInputHandlers.stream().allMatch(this::isInputValid);
    }

    protected void finishProcessing(int operations) {
        if (this.output == null) {
            return;
        }

        for (int i = 0; i < recipeItems.size(); i++) {
            ItemStack recipeItem = recipeItems.get(i);
            IInputHandler<@NotNull ItemStack> itemInputHandler = itemInputHandlers.get(i);

            itemInputHandler.use(recipeItem, operations);
        }

        for (FluidStack recipeFluid : this.recipeFluids) {
            this.fluidInputHandler.use(recipeFluid, operations);
        }
        this.outputHandler.handleOutput(this.output, operations);
    }
}
