package dev.tdnpgm.gregmek.content.recipe.lookup;

import dev.tdnpgm.gregmek.content.recipe.lookup.cache.GMInputRecipeCache;
import dev.tdnpgm.gregmek.content.recipe.lookup.cache.SingleMultipleShapelessRecipeCache;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.recipe.lookup.IRecipeLookupHandler;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public interface ISingleMultipleRecipeLookupHandler<INPUT, RECIPE extends MekanismRecipe & Predicate<List<INPUT>>, INPUT_CACHE extends SingleMultipleShapelessRecipeCache<INPUT, ?, RECIPE, ?>> extends IRecipeLookupHandler.IRecipeTypedLookupHandler<RECIPE, INPUT_CACHE> {
    default boolean containsRecipe(List<INPUT> inputsA) {
        return this.getRecipeType().getInputCache().containsInputs(this.getHandlerWorld(), inputsA);
    }

    default boolean containsRecipe(INPUT input) {
        return this.getRecipeType().getInputCache().containsInput(this.getHandlerWorld(), input);
    }

    default @Nullable RECIPE findFirstRecipe(List<INPUT> inputs) {
        return this.getRecipeType().getInputCache().findFirstRecipe(this.getHandlerWorld(), inputs);
    }

    default @Nullable RECIPE findFirstRecipeHandlers(List<IInputHandler<INPUT>> inputHandler) {
        return this.findFirstRecipe(inputHandler.stream().map(IInputHandler::getInput).toList());
    }

    interface ItemsMultipleRecipeLookupHandler<
            RECIPE extends MekanismRecipe & Predicate<List<ItemStack>>>
            extends ISingleMultipleRecipeLookupHandler<ItemStack, RECIPE, GMInputRecipeCache.Items<RECIPE>> {
    }

}
