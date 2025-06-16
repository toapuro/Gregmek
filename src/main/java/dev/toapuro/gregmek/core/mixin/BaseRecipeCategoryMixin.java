package dev.toapuro.gregmek.core.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.toapuro.gregmek.common.helper.MixinHelper;
import dev.toapuro.gregmek.core.interfaces.IHasExtraRecipeData;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.GuiTexturedElement;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEIRecipeType;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Mixin(value = BaseRecipeCategory.class, remap = false)
public abstract class BaseRecipeCategoryMixin<RECIPE> {
    @Unique
    private GuiInnerScreen gregmek$recipeTierElement;

    @Unique
    private RECIPE gregmek$recipeCache;

    @Shadow
    protected abstract <ELEMENT extends GuiTexturedElement> ELEMENT addElement(ELEMENT element);

    @Inject(method = "<init>(Lmezz/jei/api/helpers/IGuiHelper;Lmekanism/client/jei/MekanismJEIRecipeType;Lnet/minecraft/network/chat/Component;Lmezz/jei/api/gui/drawable/IDrawable;IIII)V",
            at = @At("TAIL"))
    private void init(IGuiHelper helper, MekanismJEIRecipeType<RECIPE> type, Component component, IDrawable icon, int xOffset, int yOffset, int width, int height, CallbackInfo ci) {
        gregmek$recipeTierElement = new GuiInnerScreen(
                MixinHelper.cast(BaseRecipeCategory.class, this),
                0,
                0,
                74,
                12,
                () -> {
                    Component c = Component.literal("NULL!!");
                    if (gregmek$recipeCache instanceof IHasExtraRecipeData extraRecipeData) {
                        c = Component.literal("TIER: " + extraRecipeData.gregmek$getRecipeTier().getSimpleName());
                    }
                    return Collections.singletonList(c);
                }
        );
    }

    @Inject(method = "renderElements", at = @At("TAIL"))
    private void renderElements(RECIPE recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, int x, int y, CallbackInfo ci, @Local PoseStack pose) {
        gregmek$recipeCache = recipe;

        gregmek$recipeTierElement.renderShifted(guiGraphics, x, y, 0.0F);
        gregmek$recipeTierElement.onDrawBackground(guiGraphics, x, y, 0.0F);
        int zOffset = 200;

        pose.pushPose();
        gregmek$recipeTierElement.onRenderForeground(guiGraphics, x, y, zOffset, zOffset);
        pose.popPose();
    }
}
