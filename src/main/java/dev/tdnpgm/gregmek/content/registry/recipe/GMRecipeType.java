package dev.tdnpgm.gregmek.content.registry.recipe;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.content.recipe.AlloySmeltingRecipe;
import dev.tdnpgm.gregmek.content.recipe.AssemblingRecipe;
import dev.tdnpgm.gregmek.content.recipe.BendingRecipe;
import dev.tdnpgm.gregmek.content.recipe.lookup.cache.GMInputRecipeCache;
import dev.tdnpgm.gregmek.mixin.accessor.MekanismRecipeTypeAccessor;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeDeferredRegister;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;

import java.util.function.Function;

public class GMRecipeType {
    public static final RecipeTypeDeferredRegister RECIPE_TYPES = new RecipeTypeDeferredRegister(Gregmek.MODID);
    public static final RecipeTypeRegistryObject<AssemblingRecipe, GMInputRecipeCache.ItemsFluids<AssemblingRecipe>> ASSEMBLING;
    public static final RecipeTypeRegistryObject<AlloySmeltingRecipe, GMInputRecipeCache.Items<AlloySmeltingRecipe>> ALLOY_SMELTING;
    public static final RecipeTypeRegistryObject<BendingRecipe, GMInputRecipeCache.Items<BendingRecipe>> BENDING;

    public static <RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache> MekanismRecipeType<RECIPE, INPUT_CACHE> make(String name, Function<MekanismRecipeType<RECIPE, INPUT_CACHE>, INPUT_CACHE> inputCacheCreator) {
        MekanismRecipeType<RECIPE, INPUT_CACHE> recipeType =
                MekanismRecipeTypeAccessor.invokeNew(name, (t) -> null);
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
                new GMInputRecipeCache.ItemsFluids<>(
                        recipeType,
                        AssemblingRecipe::getInputSolids,
                        AssemblingRecipe::getInputFluids,
                        AssemblingRecipe.MAX_ITEM_SLOTS,
                        AssemblingRecipe.MAX_FLUID_SLOTS
                ));
        ALLOY_SMELTING = register("alloy_smelting", (recipeType) ->
                new GMInputRecipeCache.Items<>(
                        recipeType,
                        AlloySmeltingRecipe::getInputSolids,
                        AlloySmeltingRecipe.MAX_ITEM_SLOTS
                ));
        BENDING = register("bending", (recipeType) ->
                new GMInputRecipeCache.Items<>(
                        recipeType,
                        BendingRecipe::getInputSolids,
                        BendingRecipe.MAX_ITEM_SLOTS
                ));
    }
}
