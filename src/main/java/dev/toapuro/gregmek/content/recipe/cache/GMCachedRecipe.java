package dev.toapuro.gregmek.content.recipe.cache;

import dev.toapuro.gregmek.common.utils.GregmekUtils;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BooleanSupplier;

public abstract class GMCachedRecipe<RECIPE extends MekanismRecipe> extends CachedRecipe<RECIPE> {
    protected GMCachedRecipe(RECIPE recipe, BooleanSupplier recheckAllErrors) {
        super(recipe, recheckAllErrors);
    }

    public List<ItemStack> filterRecipeItems(List<ItemStackIngredient> recipeInputItems, List<IInputHandler<@NotNull ItemStack>> inputHandlers) {
        List<ItemStack> recipeItems = GregmekUtils.makeListOf(() -> ItemStack.EMPTY, inputHandlers.size());
        ArrayList<ItemStackIngredient> itemSolidsRemain = new ArrayList<>(recipeInputItems);

        for (int i = 0; i < inputHandlers.size(); i++) {
            IInputHandler<@NotNull ItemStack> itemInputHandler = inputHandlers.get(i);
            Iterator<ItemStackIngredient> queueIterator = itemSolidsRemain.iterator();
            while (queueIterator.hasNext()) {
                ItemStackIngredient itemSolid = queueIterator.next();
                ItemStack recipeInput = itemInputHandler.getRecipeInput(itemSolid);
                if (!recipeInput.isEmpty()) {
                    recipeItems.set(i, recipeInput);
                    queueIterator.remove();
                    break;
                }
            }
        }

        return recipeItems;
    }

    @Override
    public boolean isInputValid() {
        return true;
    }

    @Override
    protected void finishProcessing(int i) {

    }
}
