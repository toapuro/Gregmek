package dev.tdnpgm.gregmek.recipes;

import dev.tdnpgm.gregmek.utils.GregmekUtils;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.AbstractInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.IInputCache;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class MultipleDoubleRecipeCache__O<INPUT_A, INGREDIENT_A extends InputIngredient<INPUT_A>,
        INPUT_B, INGREDIENT_B extends InputIngredient<INPUT_B>,
        RECIPE extends MekanismRecipe & BiPredicate<List<INPUT_A>, List<INPUT_B>>,
        CACHE_A extends IInputCache<INPUT_A, INGREDIENT_A, RECIPE>,
        CACHE_B extends IInputCache<INPUT_B, INGREDIENT_B, RECIPE>> extends AbstractInputRecipeCache<RECIPE> {
    private final List<Set<RECIPE>> complexIngredientsA;
    private final List<Set<RECIPE>> complexIngredientsB;
    private final Set<RECIPE> complexRecipes = new HashSet<>();
    private final Function<RECIPE, List<INGREDIENT_A>> inputsAExtractor;
    private final Function<RECIPE, List<INGREDIENT_B>> inputsBExtractor;
    private final List<CACHE_A> cachesA;
    private final List<CACHE_B> cachesB;

    protected MultipleDoubleRecipeCache__O(MekanismRecipeType<RECIPE, ?> recipeType,
                                           Function<RECIPE, List<INGREDIENT_A>> inputsAExtractor,
                                           List<CACHE_A> cachesA,
                                           Function<RECIPE, List<INGREDIENT_B>> inputsBExtractor,
                                           List<CACHE_B> cachesB) {
        super(recipeType);
        this.inputsAExtractor = inputsAExtractor;
        this.inputsBExtractor = inputsBExtractor;
        this.complexIngredientsA = GregmekUtils.generateNList(HashSet::new, cachesA.size());
        this.complexIngredientsB = GregmekUtils.generateNList(HashSet::new, cachesB.size());
        this.cachesA = cachesA;
        this.cachesB = cachesB;
    }

    public void clear() {
        super.clear();
        this.cachesA.forEach(IInputCache::clear);
        this.cachesB.forEach(IInputCache::clear);

        this.complexIngredientsB.clear();
        this.complexRecipes.clear();
    }

    public static <INPUT, CACHE> Map<INPUT, CACHE> getMapIC(List<INPUT> inputs, List<CACHE> caches) {
        Map<INPUT, CACHE> map = new HashMap<>();

        for (int i = 0; i < inputs.size(); i++) {
            INPUT input = inputs.get(i);
            CACHE cache = caches.get(i);

            map.put(input, cache);
        }

        return map;
    }

    public static <INPUT, CACHE, RECIPE, INGREDIENT> Map<INPUT, CacheExtractorMap<CACHE, RECIPE, INGREDIENT>> getMapICRI(List<INPUT> inputs, List<CACHE> caches, Function<RECIPE, List<INGREDIENT>> inputsExtractor, List<Set<RECIPE>> complexIngredients) {
        Map<INPUT, CacheExtractorMap<CACHE, RECIPE, INGREDIENT>> map = new HashMap<>();

        for (int i = 0; i < inputs.size(); i++) {
            int ingredientIndex = i;

            INPUT input = inputs.get(i);
            CACHE cache = caches.get(i);
            Set<RECIPE> recipes = complexIngredients.get(i);
            Function<RECIPE, INGREDIENT> extractor = recipe -> inputsExtractor.apply(recipe).get(ingredientIndex);

            map.put(input, new CacheExtractorMap<>(cache, extractor, recipes));
        }

        return map;
    }

    public static <CACHE, RECIPE, INGREDIENT> Map<CACHE, CacheExtractorMap<CACHE, RECIPE, INGREDIENT>> getMapCRI(List<CACHE> caches, Function<RECIPE, List<INGREDIENT>> inputsExtractor, List<Set<RECIPE>> complexIngredients) {
        Map<CACHE, CacheExtractorMap<CACHE, RECIPE, INGREDIENT>> map = new HashMap<>();

        for (int i = 0; i < caches.size(); i++) {
            int ingredientIndex = i;

            CACHE cache = caches.get(i);
            Set<RECIPE> recipes = complexIngredients.get(i);
            Function<RECIPE, INGREDIENT> extractor = recipe -> inputsExtractor.apply(recipe).get(ingredientIndex);

            map.put(cache, new CacheExtractorMap<>(cache, extractor, recipes));
        }

        return map;
    }

    public boolean containsInputA(@Nullable Level world, List<INPUT_A> inputs) {
        Map<INPUT_A, CacheExtractorMap<CACHE_A, RECIPE, INGREDIENT_A>> inputMapSet = getMapICRI(inputs, cachesA, inputsAExtractor, complexIngredientsA);
        for (INPUT_A inputA : inputMapSet.keySet()) {
            CacheExtractorMap<CACHE_A, RECIPE, INGREDIENT_A> map = inputMapSet.get(inputA);

            if(!this.containsInput(world, inputA, map.inputExtractor, map.cache, map.complexIngredient)) {
                return false;
            }
        }
        return true;
    }
    public boolean containsInputA(@Nullable Level world, INPUT_A input, int i) {
        CACHE_A cacheA = cachesA.get(i);
        Set<RECIPE> complexIngredient = complexIngredientsA.get(i);

        return this.containsInput(world, input, recipe -> inputsAExtractor.apply(recipe).get(i), cacheA, complexIngredient);
    }

    public boolean containsInputB(@Nullable Level world, List<INPUT_B> inputs) {
        Map<INPUT_B, CacheExtractorMap<CACHE_B, RECIPE, INGREDIENT_B>> inputMapSet = getMapICRI(inputs, cachesB, inputsBExtractor, complexIngredientsB);
        for (INPUT_B inputB : inputMapSet.keySet()) {
            CacheExtractorMap<CACHE_B, RECIPE, INGREDIENT_B> map = inputMapSet.get(inputB);

            if(!this.containsInput(world, inputB, map.inputExtractor, map.cache, map.complexIngredient)) {
                return false;
            }
        }

        return true;
    }

    public boolean containsInputAB(@Nullable Level world, List<INPUT_A> inputsA, List<INPUT_B> inputsB) {
        return containsGrouping(world, inputsA, inputsAExtractor, cachesA, complexIngredientsA,
                inputsB, inputsBExtractor, cachesB, complexIngredientsB);
    }

    public boolean containsInputBA(@Nullable Level world, List<INPUT_A> inputsA, List<INPUT_B> inputsB) {
        return containsGrouping(world, inputsB, inputsBExtractor, cachesB, complexIngredientsB,
                inputsA, inputsAExtractor, cachesA, complexIngredientsA);
    }

    private <INPUT_1, INGREDIENT_1 extends InputIngredient<INPUT_1>, CACHE_1 extends IInputCache<INPUT_1, INGREDIENT_1, RECIPE>, INPUT_2, INGREDIENT_2 extends InputIngredient<INPUT_2>, CACHE_2 extends IInputCache<INPUT_2, INGREDIENT_2, RECIPE>> boolean containsGrouping(@Nullable Level world, List<INPUT_1> inputs1, Function<RECIPE, List<INGREDIENT_1>> inputs1Extractor, List<CACHE_1> caches1, List<Set<RECIPE>> complexIngredients1, List<INPUT_2> inputs2, Function<RECIPE, List<INGREDIENT_2>> inputs2Extractor, List<CACHE_2> caches2, List<Set<RECIPE>> complexIngredients2) {
        Map<INPUT_1, CacheExtractorMap<CACHE_1, RECIPE, INGREDIENT_1>> input1MapSet = getMapICRI(inputs1, caches1, inputs1Extractor, complexIngredients1);

        for (INPUT_1 input1 : input1MapSet.keySet()) {
            CacheExtractorMap<CACHE_1, RECIPE, INGREDIENT_1> map1 = input1MapSet.get(input1);

            if(!containsInput(world, input1, map1.inputExtractor, map1.cache, map1.complexIngredient)) {
                continue;
            }

            Map<INPUT_2, CacheExtractorMap<CACHE_2, RECIPE, INGREDIENT_2>> input2MapSet = getMapICRI(inputs2, caches2, inputs2Extractor, complexIngredients2);
            for (INPUT_2 input2 : input2MapSet.keySet()) {
                CacheExtractorMap<CACHE_2, RECIPE, INGREDIENT_2> map2 = input2MapSet.get(input2);

                if(!containsInput(world, input2, map2.inputExtractor, map2.cache, map2.complexIngredient)) {
                    continue;
                }

                initCacheIfNeeded(world);

                if(!map1.cache.contains(input1, (recipe) -> map2.inputExtractor.apply(recipe).testType(input2)) ||
                        map1.complexIngredient.stream().anyMatch((recipe) -> (
                                map1.inputExtractor.apply(recipe).testType(input1) &&
                                map2.inputExtractor.apply(recipe).testType(input2))
                        )) {
                    return false;
                }
            }
        }

        return true;
    }

    public @Nullable RECIPE findFirstRecipe(@Nullable Level world, List<INPUT_A> inputsA, List<INPUT_B> inputsB) {
        this.initCacheIfNeeded(world);
        Predicate<RECIPE> matchPredicate = (r) -> r.test(inputsA, inputsB);
        Map<INPUT_A, CACHE_A> inputACache = getMapIC(inputsA, cachesA);
        for (INPUT_A inputA : inputACache.keySet()) {
            CACHE_A cacheA = inputACache.get(inputA);
            RECIPE recipe = cacheA.findFirstRecipe(inputA, matchPredicate);
            if (recipe != null) {
                return recipe;
            }
        }
        return this.findFirstRecipe(this.complexRecipes, matchPredicate);
    }

    protected void initCache(List<RECIPE> recipes) {
        for(RECIPE recipe : recipes) {
            boolean complexA = false;
            boolean complexB = false;

            Map<CACHE_A, CacheExtractorMap<CACHE_A, RECIPE, INGREDIENT_A>> mapASet = getMapCRI(cachesA, inputsAExtractor, complexIngredientsA);
            for (CACHE_A cacheA : mapASet.keySet()) {
                CacheExtractorMap<CACHE_A, RECIPE, INGREDIENT_A> map = mapASet.get(cacheA);
                complexA |= cacheA.mapInputs(recipe, map.inputExtractor.apply(recipe));

                if(complexA) {
                    map.complexIngredient.add(recipe);
                }
            }

            Map<CACHE_B, CacheExtractorMap<CACHE_B, RECIPE, INGREDIENT_B>> mapBSet = getMapCRI(cachesB, inputsBExtractor, complexIngredientsB);
            for (CACHE_B cacheB : mapBSet.keySet()) {
                CacheExtractorMap<CACHE_B, RECIPE, INGREDIENT_B> map = mapBSet.get(cacheB);
                complexB |= cacheB.mapInputs(recipe, map.inputExtractor.apply(recipe));

                if(complexB) {
                    map.complexIngredient.add(recipe);
                }
            }

            if (complexA || complexB) {
                this.complexRecipes.add(recipe);
            }
        }

    }
    public record CacheExtractorMap<CACHE, RECIPE, INGREDIENT>(CACHE cache, Function<RECIPE, INGREDIENT> inputExtractor, Set<RECIPE> complexIngredient) {
    }
}
