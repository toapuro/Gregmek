package dev.tdnpgm.gregmek.registry.recipe;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.mixin.accessor.MekanismRecipeTypeAccessor;
import dev.tdnpgm.gregmek.recipes.AssemblingRecipe;
import dev.tdnpgm.gregmek.recipes.GregmekInputRecipeCache;
import io.netty.channel.Channel;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeDeferredRegister;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class GregmekRecipeType{
    public static final RecipeTypeDeferredRegister RECIPE_TYPES = new RecipeTypeDeferredRegister(Gregmek.MODID);
    public static final RecipeTypeRegistryObject<AssemblingRecipe, GregmekInputRecipeCache.ItemsFluids<AssemblingRecipe>> ASSEMBLING;

    public static <RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache> MekanismRecipeType<RECIPE, INPUT_CACHE> make(String name, Function<MekanismRecipeType<RECIPE, INPUT_CACHE>, INPUT_CACHE> inputCacheCreator) {
//        MekanismRecipeType<RECIPE, INPUT_CACHE> recipeType = MekanismRecipeTypeAccessor.invokeInit(name, inputCacheCreator);
//        if(recipeType instanceof MekanismRecipeTypeAccessor accessor) {
//            accessor.setRegistryName(Gregmek.rl(name));
//            accessor.setInputCache(inputCacheCreator.apply(recipeType));
//        }

        try {
            MekanismRecipeType<RECIPE, INPUT_CACHE> recipeType = (MekanismRecipeType<RECIPE, INPUT_CACHE>) UnsafeAccess.UNSAFE.allocateInstance(MekanismRecipeType.class);
            Field registryName = MekanismRecipeType.class.getDeclaredField("registryName");
            registryName.setAccessible(true);
            registryName.set(recipeType, Gregmek.rl(name));

            Field cachedRecipes = MekanismRecipeType.class.getDeclaredField("cachedRecipes");
            cachedRecipes.setAccessible(true);
            cachedRecipes.set(recipeType, Collections.emptyList());


            Field inputCache = MekanismRecipeType.class.getDeclaredField("inputCache");
            inputCache.setAccessible(true);
            inputCache.set(recipeType, inputCacheCreator.apply(recipeType));

            return recipeType;
        } catch (InstantiationException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static <RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache> RecipeTypeRegistryObject<RECIPE, INPUT_CACHE> register(String name, Function<MekanismRecipeType<RECIPE, INPUT_CACHE>, INPUT_CACHE> inputCacheCreator) {
        return RECIPE_TYPES.register(name, () -> make(name, inputCacheCreator));
    }

    static {
        ASSEMBLING = register("assembling", (recipeType) ->
                new GregmekInputRecipeCache.ItemsFluids<>(recipeType, AssemblingRecipe::getInputSolids, AssemblingRecipe::getInputFluids, AssemblingRecipe.itemSlots, AssemblingRecipe.fluidSlots));
    }
}
