package dev.tdnpgm.gregmek.mixin;

import dev.tdnpgm.gregmek.recipes.jei.AssemblerRecipeCategory;
import dev.tdnpgm.gregmek.registry.GregmekJEIRecipeTypes;
import mekanism.client.jei.MekanismJEI;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = MekanismJEI.class, remap = false)
public class MekanismJEIMixin {
    @Inject(method = "registerCategories", at = @At("TAIL"))
    public void registerCategories(IRecipeCategoryRegistration registry, CallbackInfo ci) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new AssemblerRecipeCategory(guiHelper, GregmekJEIRecipeTypes.ASSEMBLING));
    }
}
