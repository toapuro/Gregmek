package dev.toapuro.gregmek.core.hooks.hook;

import dev.toapuro.gregmek.core.hooks.IMixinHook;
import mekanism.client.jei.MekanismJEIRecipeType;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.network.chat.Component;

public interface IRecipeCategoryMixinHook extends IMixinHook {
    default <RECIPE> void initElements(IGuiHelper helper, MekanismJEIRecipeType<RECIPE> type, Component component, IDrawable icon, int xOffset, int yOffset, int width, int height) {
    }

}
