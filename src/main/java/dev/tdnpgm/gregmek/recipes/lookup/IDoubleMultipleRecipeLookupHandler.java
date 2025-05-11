package dev.tdnpgm.gregmek.recipes.lookup;

import dev.tdnpgm.gregmek.recipes.lookup.cache.DoubleMultipleShapelessRecipeCache;
import dev.tdnpgm.gregmek.recipes.lookup.cache.GregmekInputRecipeCache;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.recipe.lookup.IRecipeLookupHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiPredicate;

public interface IDoubleMultipleRecipeLookupHandler<INPUT_A, INPUT_B, RECIPE extends MekanismRecipe & BiPredicate<List<INPUT_A>, List<INPUT_B>>, INPUT_CACHE extends DoubleMultipleShapelessRecipeCache<INPUT_A, ?, INPUT_B, ?, RECIPE, ?, ?>> extends IRecipeLookupHandler.IRecipeTypedLookupHandler<RECIPE, INPUT_CACHE> {
    default boolean containsRecipeAB(List<INPUT_A> inputsA, List<INPUT_B> inputsB) {
        return this.getRecipeType().getInputCache().containsInputAB(this.getHandlerWorld(), inputsA, inputsB);
    }

    default boolean containsRecipeBA(List<INPUT_A> inputsA, List<INPUT_B> inputsB) {
        return this.getRecipeType().getInputCache().containsInputBA(this.getHandlerWorld(), inputsA, inputsB);
    }

    default boolean containsRecipeA(List<INPUT_A> inputs) {
        return this.getRecipeType().getInputCache().containsInputsA(this.getHandlerWorld(), inputs);
    }

    default boolean containsRecipeB(List<INPUT_B> inputs) {
        return this.getRecipeType().getInputCache().containsInputsB(this.getHandlerWorld(), inputs);
    }

    default boolean containsRecipeA(INPUT_A input) {
        return this.getRecipeType().getInputCache().containsInputA(this.getHandlerWorld(), input);
    }

    default boolean containsRecipeB(INPUT_B input) {
        return this.getRecipeType().getInputCache().containsInputB(this.getHandlerWorld(), input);
    }

    default @Nullable RECIPE findFirstRecipe(List<INPUT_A> inputsA, List<INPUT_B> inputsB) {
        return this.getRecipeType().getInputCache().findFirstRecipe(this.getHandlerWorld(), inputsA, inputsB);
    }

    default @Nullable RECIPE findFirstRecipeHandlers(List<IInputHandler<INPUT_A>> inputAHandler, List<IInputHandler<INPUT_B>> inputBHandler) {
        return this.findFirstRecipe(inputAHandler.stream().map(IInputHandler::getInput).toList(), inputBHandler.stream().map(IInputHandler::getInput).toList());
    }

    interface ItemsFluidsMultipleRecipeLookupHandler<
            RECIPE extends MekanismRecipe & BiPredicate<List<ItemStack>, List<FluidStack>>>
            extends IDoubleMultipleRecipeLookupHandler<ItemStack, FluidStack, RECIPE, GregmekInputRecipeCache.ItemsFluids<RECIPE>> {
    }

}
