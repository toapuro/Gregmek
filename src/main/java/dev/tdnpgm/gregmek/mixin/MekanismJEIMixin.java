package dev.tdnpgm.gregmek.mixin;

import dev.tdnpgm.gregmek.recipes.jei.AlloySmelterRecipeCategory;
import dev.tdnpgm.gregmek.recipes.jei.AssemblerRecipeCategory;
import dev.tdnpgm.gregmek.registry.GMJEIRecipeTypes;
import mekanism.client.jei.MekanismJEI;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MekanismJEI.class, remap = false)
public class MekanismJEIMixin {
    @Inject(method = "registerCategories", at = @At("TAIL"))
    public void registerCategories(IRecipeCategoryRegistration registry, CallbackInfo ci) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new AssemblerRecipeCategory(guiHelper, GMJEIRecipeTypes.ASSEMBLING));
        registry.addRecipeCategories(new AlloySmelterRecipeCategory(guiHelper, GMJEIRecipeTypes.ALLOY_SMELTER));
    }
}
