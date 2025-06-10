package dev.toapuro.gregmek.client.gui.modern;

import mekanism.api.providers.IItemProvider;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEIRecipeType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import org.jetbrains.annotations.NotNull;

public abstract class GMRecipeCategory<RECIPE> extends BaseRecipeCategory<RECIPE> {
    protected GMRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<RECIPE> recipeType, IItemProvider provider, int xOffset, int yOffset, int width, int height) {
        super(helper, recipeType, provider, xOffset, yOffset, width, height);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder recipeLayoutBuilder, @NotNull RECIPE recipe, @NotNull IFocusGroup focusGroup) {

    }
}
