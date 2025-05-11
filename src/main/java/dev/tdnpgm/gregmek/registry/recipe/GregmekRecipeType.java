package dev.tdnpgm.gregmek.registry.recipe;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.mixin.accessor.MekanismRecipeTypeAccessor;
import dev.tdnpgm.gregmek.recipes.AssemblingRecipe;
import dev.tdnpgm.gregmek.recipes.lookup.cache.GregmekInputRecipeCache;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeDeferredRegister;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;

import java.util.function.Function;

public class GregmekRecipeType{
    public static final RecipeTypeDeferredRegister RECIPE_TYPES = new RecipeTypeDeferredRegister(Gregmek.MODID);
    public static final RecipeTypeRegistryObject<AssemblingRecipe, GregmekInputRecipeCache.ItemsFluids<AssemblingRecipe>> ASSEMBLING;

    public static <RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache> MekanismRecipeType<RECIPE, INPUT_CACHE> make(String name, Function<MekanismRecipeType<RECIPE, INPUT_CACHE>, INPUT_CACHE> inputCacheCreator) {
        MekanismRecipeType<RECIPE, INPUT_CACHE> recipeType =
                MekanismRecipeTypeAccessor.invokeInit(name, (t) -> null);
        if(recipeType instanceof MekanismRecipeTypeAccessor accessor) {
            accessor.setRegistryName(Gregmek.rl(name));
            accessor.setInputCache(inputCacheCreator.apply(recipeType));
        }

        return recipeType;
    }

    private static <RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache> RecipeTypeRegistryObject<RECIPE, INPUT_CACHE> register(String name, Function<MekanismRecipeType<RECIPE, INPUT_CACHE>, INPUT_CACHE> inputCacheCreator) {
        return RECIPE_TYPES.register(name, () -> make(name, inputCacheCreator));
    }

    static {
        ASSEMBLING = register("assembling", (recipeType) ->
                new GregmekInputRecipeCache.ItemsFluids<>(
                        recipeType,
                        AssemblingRecipe::getInputSolids,
                        AssemblingRecipe::getInputFluids,
                        AssemblingRecipe.itemSlots,
                        AssemblingRecipe.fluidSlots
                ));
    }
}
