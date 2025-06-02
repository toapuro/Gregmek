package dev.toapuro.gregmek.content.recipe.cache;

import dev.toapuro.gregmek.common.utils.GregmekUtils;
import dev.toapuro.gregmek.content.recipe.AlloySmeltingRecipe;
import dev.toapuro.gregmek.content.recipe.BendingRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SingleShapelessInputsCachedRecipe<
        INPUT,
        OUTPUT,
        RECIPE extends MekanismRecipe & Predicate<List<INPUT>>> extends CachedRecipe<RECIPE> {
    private final IOutputHandler<OUTPUT> outputHandler;
    private final List<IInputHandler<INPUT>> inputHandlers;
    private final List<INPUT> recipeItems;

    private final Predicate<INPUT> inputEmptyCheck;
    private final Function<List<INPUT>, OUTPUT> outputGetter;
    private final Supplier<List<? extends InputIngredient<INPUT>>> inputsSupplier;
    private final INPUT emptyInput;
    @Nullable
    private OUTPUT output;

    public SingleShapelessInputsCachedRecipe(RECIPE recipe, BooleanSupplier recheckAllErrors, List<IInputHandler<INPUT>> inputHandlers, int maxInputs, IOutputHandler<OUTPUT> outputHandler, Predicate<INPUT> inputEmptyCheck, INPUT emptyInput, Function<List<INPUT>, OUTPUT> outputGetter, Supplier<List<? extends InputIngredient<@NotNull INPUT>>> inputsSupplier) {
        super(recipe, recheckAllErrors);
        this.recipeItems = GregmekUtils.makeListOf(() -> emptyInput, maxInputs);
        this.inputHandlers = Objects.requireNonNull(inputHandlers, "Item input handler cannot be null.");
        this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
        this.emptyInput = Objects.requireNonNull(emptyInput, "Empty input cannot be null.");
        this.inputEmptyCheck = Objects.requireNonNull(inputEmptyCheck, "Empty check cannot be null.");
        this.outputGetter = Objects.requireNonNull(outputGetter, "Output getter cannot be null.");
        this.inputsSupplier = Objects.requireNonNull(inputsSupplier, "Inputs supplier cannot be null.");
    }

    public static SingleShapelessInputsCachedRecipe<ItemStack, ItemStack, AlloySmeltingRecipe> alloySmelter(AlloySmeltingRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> mainInputHandler, IInputHandler<@NotNull ItemStack> extraInputHandler, IOutputHandler<@NotNull ItemStack> outputHandler) {
        Objects.requireNonNull(recipe);
        return new SingleShapelessInputsCachedRecipe<ItemStack, ItemStack, AlloySmeltingRecipe>(
                recipe,
                recheckAllErrors,
                List.of(mainInputHandler, extraInputHandler),
                AlloySmeltingRecipe.MAX_ITEM_SLOTS,
                outputHandler,
                ItemStack::isEmpty,
                ItemStack.EMPTY,
                recipe::getOutput,
                recipe::getInputSolids
        );
    }

    public static SingleShapelessInputsCachedRecipe<ItemStack, ItemStack, BendingRecipe> bender(BendingRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> mainInputHandler, IInputHandler<@NotNull ItemStack> extraInputHandler, IOutputHandler<@NotNull ItemStack> outputHandler) {
        Objects.requireNonNull(recipe);
        return new SingleShapelessInputsCachedRecipe<ItemStack, ItemStack, BendingRecipe>(
                recipe,
                recheckAllErrors,
                List.of(mainInputHandler, extraInputHandler),
                AlloySmeltingRecipe.MAX_ITEM_SLOTS,
                outputHandler,
                ItemStack::isEmpty,
                ItemStack.EMPTY,
                recipe::getOutput,
                recipe::getInputSolids
        );
    }

    protected void calculateOperationsThisTick(@NotNull OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            ArrayList<InputIngredient<INPUT>> recipeRemain = new ArrayList<>(inputsSupplier.get());

            this.recipeItems.replaceAll(itemStack -> emptyInput);
            for (int i = 0; i < this.inputHandlers.size(); i++) {
                IInputHandler<INPUT> inputHandler = this.inputHandlers.get(i);
                Iterator<InputIngredient<INPUT>> queueIterator = recipeRemain.iterator();
                while (queueIterator.hasNext()) {
                    InputIngredient<INPUT> ingredient = queueIterator.next();
                    INPUT recipeInput = inputHandler.getRecipeInput(ingredient);
                    if (!inputEmptyCheck.test(recipeInput)) {
                        this.recipeItems.set(i, recipeInput);
                        queueIterator.remove();
                        break;
                    }
                }
            }

            if (!recipeRemain.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                this.output = outputGetter.apply(this.recipeItems);
                this.outputHandler.calculateOperationsCanSupport(tracker, this.output);
            }
        }
    }

    public boolean isInputValid() {
        List<INPUT> inputs = this.inputHandlers.stream().map(IInputHandler::getInput).toList();
        if (inputs.stream().allMatch(inputEmptyCheck)) {
            return false;
        }

        return this.recipe.test(inputs);
    }

    protected void finishProcessing(int operations) {
        if (this.output == null) {
            return;
        }

        for (int i = 0; i < recipeItems.size(); i++) {
            INPUT recipeInput = recipeItems.get(i);
            IInputHandler<INPUT> itemInputHandler = inputHandlers.get(i);

            itemInputHandler.use(recipeInput, operations);
        }

        this.outputHandler.handleOutput(this.output, operations);
    }
}
