package dev.tdnpgm.gregmek.recipes.lookup.cache;

import dev.tdnpgm.gregmek.utils.GregmekUtils;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.DoubleInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.FluidInputCache;
import mekanism.common.recipe.lookup.cache.type.ItemInputCache;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class GregmekInputRecipeCache {
    public static class ItemFluid<
            RECIPE extends MekanismRecipe & BiPredicate<ItemStack, FluidStack>> extends DoubleInputRecipeCache<ItemStack, ItemStackIngredient, FluidStack, FluidStackIngredient, RECIPE, ItemInputCache<RECIPE>, FluidInputCache<RECIPE>> {
        public ItemFluid(MekanismRecipeType<RECIPE, ?> recipeType, Function<RECIPE, ItemStackIngredient> inputAExtractor, Function<RECIPE, FluidStackIngredient> inputBExtractor) {
            super(recipeType, inputAExtractor, new ItemInputCache<>(), inputBExtractor, new FluidInputCache<>());
        }
    }

    public static class ItemsFluids<
            RECIPE extends MekanismRecipe & BiPredicate<List<ItemStack>, List<FluidStack>>> extends DoubleMultipleShapelessRecipeCache<ItemStack, ItemStackIngredient, FluidStack, FluidStackIngredient, RECIPE, ItemInputCache<RECIPE>, FluidInputCache<RECIPE>> {
        public ItemsFluids(MekanismRecipeType<RECIPE, ?> recipeType, Function<RECIPE, List<ItemStackIngredient>> inputsAExtractor, Function<RECIPE, List<FluidStackIngredient>> inputsBExtractor, int maxSolidInputs, int maxFluidSlots) {
            super(
                    recipeType,
                    inputsAExtractor,
                    GregmekUtils.makeListOf(ItemInputCache::new, maxSolidInputs),
                    inputsBExtractor,
                    GregmekUtils.makeListOf(FluidInputCache::new, maxFluidSlots)
            );
        }
    }

    public static class Items<
            RECIPE extends MekanismRecipe & Predicate<List<ItemStack>>> extends SingleMultipleShapelessRecipeCache<ItemStack, ItemStackIngredient, RECIPE, ItemInputCache<RECIPE>> {
        public Items(MekanismRecipeType<RECIPE, ?> recipeType, Function<RECIPE, List<ItemStackIngredient>> inputsAExtractor, int maxSolidInputs) {
            super(
                    recipeType,
                    inputsAExtractor,
                    GregmekUtils.makeListOf(ItemInputCache::new, maxSolidInputs)
            );
        }
    }
}
