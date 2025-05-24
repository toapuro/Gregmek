package dev.tdnpgm.gregmek.mixin.accessor;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.function.Function;

@Mixin(value = MekanismRecipeType.class, remap = false)
public interface MekanismRecipeTypeAccessor {
    @Invoker("<init>")
    static <RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache> MekanismRecipeType<RECIPE, INPUT_CACHE> invokeInit(String name, Function<MekanismRecipeType<RECIPE, INPUT_CACHE>, INPUT_CACHE> inputCacheCreator) {
        return null;
    }

    @Mutable
    @Accessor("registryName")
    void setRegistryName(ResourceLocation resourceLocation);

    @Accessor("cachedRecipes")
    <RECIPE extends MekanismRecipe>  List<RECIPE> getCachedRecipes();

    @Mutable
    @Accessor("inputCache")
    <INPUT_CACHE extends IInputRecipeCache> void setInputCache(INPUT_CACHE inputCache);
}
