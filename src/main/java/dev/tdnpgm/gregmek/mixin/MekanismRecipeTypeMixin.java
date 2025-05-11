package dev.tdnpgm.gregmek.mixin;

import mekanism.common.recipe.MekanismRecipeType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MekanismRecipeType.class, remap = false)
public class MekanismRecipeTypeMixin {
//    @Inject(method = "clearCache()V", at = @At("TAIL"))
//    private static void clearCache(CallbackInfo ci) {
//        for(IMekanismRecipeTypeProvider<?, ?> recipeTypeProvider : GregmekRecipeType.RECIPE_TYPES.getAllRecipeTypes()) {
//            MekanismRecipeType<?, ?> recipeType = recipeTypeProvider.getRecipeType();
//            MekanismRecipeTypeAccessor accessor = (MekanismRecipeTypeAccessor) recipeType;
//            accessor.clearCaches();
//        }
//    }
}
