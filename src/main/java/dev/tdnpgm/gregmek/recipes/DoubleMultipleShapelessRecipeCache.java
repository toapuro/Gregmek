package dev.tdnpgm.gregmek.recipes;

import dev.tdnpgm.gregmek.Gregmek;
import mekanism.api.recipes.MekanismRecipe;
import dev.tdnpgm.gregmek.utils.GregmekUtils;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.AbstractInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.IInputCache;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class DoubleMultipleShapelessRecipeCache<
        INPUT_TYPE_A, INGREDIENT_TYPE_A extends InputIngredient<INPUT_TYPE_A>,
        INPUT_TYPE_B, INGREDIENT_TYPE_B extends InputIngredient<INPUT_TYPE_B>,
        RECIPE extends MekanismRecipe & BiPredicate<List<INPUT_TYPE_A>, List<INPUT_TYPE_B>>,
        CACHE_A extends IInputCache<INPUT_TYPE_A, INGREDIENT_TYPE_A, RECIPE>,
        CACHE_B extends IInputCache<INPUT_TYPE_B, INGREDIENT_TYPE_B, RECIPE>> extends AbstractInputRecipeCache<RECIPE> {
    private final List<Set<RECIPE>> complexIngredientsA;
    private final List<Set<RECIPE>> complexIngredientsB;
    private final Set<RECIPE> complexRecipes = new HashSet<>();
    private final Function<RECIPE, List<INGREDIENT_TYPE_A>> inputsAExtractor;
    private final Function<RECIPE, List<INGREDIENT_TYPE_B>> inputsBExtractor;
    private final List<CACHE_A> cachesA;
    private final List<CACHE_B> cachesB;

    protected DoubleMultipleShapelessRecipeCache(MekanismRecipeType<RECIPE, ?> recipeType, Function<RECIPE, List<INGREDIENT_TYPE_A>> inputsAExtractor, List<CACHE_A> cachesA, Function<RECIPE, List<INGREDIENT_TYPE_B>> inputsBExtractor, List<CACHE_B> cachesB) {
        super(recipeType);
//        Gregmek.DEBUG_LOGGER.info("Initiating DoubleMultipleShapelessRecipeCache {}", recipeType.toString());
        this.complexIngredientsA = Collections.unmodifiableList(
                GregmekUtils.generateNList(HashSet::new, cachesA.size())
        );
        this.complexIngredientsB = Collections.unmodifiableList(
                GregmekUtils.generateNList(HashSet::new, cachesB.size())
        );
        this.inputsAExtractor = inputsAExtractor;
        this.inputsBExtractor = inputsBExtractor;
        this.cachesA = Collections.unmodifiableList(cachesA);
        this.cachesB = Collections.unmodifiableList(cachesB);
    }

    public void clear() {
        super.clear();
        this.cachesA.forEach(IInputCache::clear);
        this.cachesB.forEach(IInputCache::clear);
        this.complexIngredientsA.forEach(Set::clear);
        this.complexIngredientsB.forEach(Set::clear);
        this.complexRecipes.clear();
    }

    public boolean containsInputsA(@Nullable Level world, List<INPUT_TYPE_A> inputs) {
        return this.containsInputs(world, inputs, inputsAExtractor, cachesA);
    }

    public boolean containsInputA(@Nullable Level world, INPUT_TYPE_A input) {
        return this.containsInput(world, input, inputsAExtractor, cachesA);
    }

    public boolean containsInputsB(@Nullable Level world, List<INPUT_TYPE_B> inputs) {
        return this.containsInputs(world, inputs, inputsBExtractor, cachesB);
    }

    public boolean containsInputB(@Nullable Level world, INPUT_TYPE_B input) {
        return this.containsInput(world, input, inputsBExtractor, cachesB);
    }

    public boolean containsInputAB(@Nullable Level world, List<INPUT_TYPE_A> inputA, List<INPUT_TYPE_B> inputB) {
        return this.containsInputsPairing(world, inputA, inputsAExtractor, cachesA, complexIngredientsA, inputB, inputsBExtractor, cachesB, complexIngredientsB);
    }

    public boolean containsInputBA(@Nullable Level world, List<INPUT_TYPE_A> inputA, List<INPUT_TYPE_B> inputB) {
        return this.containsInputsPairing(world, inputB, inputsBExtractor, cachesB, complexIngredientsB, inputA, inputsAExtractor, cachesA, complexIngredientsA);
    }

    public <INGREDIENT_TYPE> Function<RECIPE, INGREDIENT_TYPE> getInputExtractor(Function<RECIPE, List<INGREDIENT_TYPE>> inputsExtractor, int index) {
        return recipe -> inputsExtractor.apply(recipe).get(index);
    }

    protected <INPUT_TYPE_1, INGREDIENT_TYPE_1 extends InputIngredient<INPUT_TYPE_1>, CACHE_1 extends IInputCache<INPUT_TYPE_1, INGREDIENT_TYPE_1, RECIPE>,
            INPUT_TYPE_2, INGREDIENT_TYPE_2 extends InputIngredient<INPUT_TYPE_2>, CACHE_2 extends IInputCache<INPUT_TYPE_2, INGREDIENT_TYPE_2, RECIPE>>
            boolean containsInputsPairing(@Nullable Level world, List<INPUT_TYPE_1> inputs1, Function<RECIPE, List<INGREDIENT_TYPE_1>> inputs1Extractor, List<CACHE_1> caches1, List<Set<RECIPE>> complexIngredients1, List<INPUT_TYPE_2> inputs2, Function<RECIPE, List<INGREDIENT_TYPE_2>> inputs2Extractor, List<CACHE_2> caches2, List<Set<RECIPE>> complexIngredients2) {
        return containsInputs(world, inputs1, inputs1Extractor, caches1) &&
                containsInputs(world, inputs2, inputs2Extractor, caches2);

//        for (int i = 0; i < inputs1.size(); i++) {
//            INPUT_TYPE_1 input = inputs1.get(i);
//            int matched = 0;
//
//            for (int j = 0; j < caches1.size(); j++) {
//                CACHE_1 cache = caches1.get(i);
//
//                if(containsInput(world, input, getInputExtractor(inputs1Extractor, i), cache, complexRecipes)) {
//                    matched++;
//                }
//            }
//
//            if(matched == 0) {
//                return false;
//            }
//        }
//
//        return true;

    }


    public <INPUT_TYPE, INGREDIENT_TYPE extends InputIngredient<INPUT_TYPE>,
            CACHE extends IInputCache<INPUT_TYPE, INGREDIENT_TYPE, RECIPE>> boolean containsInput(@Nullable Level world, INPUT_TYPE input, Function<RECIPE, List<INGREDIENT_TYPE>> inputsExtractor, List<CACHE> caches) {
        Gregmek.DEBUG_LOGGER.info("containsInput {}", input.getClass().getName());
        for (int i = 0; i < caches.size(); i++) {
            CACHE cache = caches.get(i);
            if(containsInput(world, input, getInputExtractor(inputsExtractor, i), cache, complexRecipes)) {
                Gregmek.DEBUG_LOGGER.info("true");
                return true;
            }
        }

        return false;
    }

    public <INPUT_TYPE, INGREDIENT_TYPE extends InputIngredient<INPUT_TYPE>,
            CACHE extends IInputCache<INPUT_TYPE, INGREDIENT_TYPE, RECIPE>> boolean containsInputs(@Nullable Level world, List<INPUT_TYPE> inputs, Function<RECIPE, List<INGREDIENT_TYPE>> inputsExtractor, List<CACHE> caches) {
//        Gregmek.DEBUG_LOGGER.info("containsInputs {}", inputs.size());
        this.initCacheIfNeeded(world);

        for (int i = 0; i < inputs.size(); i++) {
            INPUT_TYPE input = inputs.get(i);
            int matched = 0;

            for (int j = 0; j < caches.size(); j++) {
                CACHE cache = caches.get(i);
                if(containsInput(world, input, getInputExtractor(inputsExtractor, i), cache, complexRecipes)) {
                    matched++;
                }
            }

            if(matched == 0) {
                return false;
            }
        }

        return true;
    }

    public @Nullable RECIPE findFirstRecipe(@Nullable Level world, List<INPUT_TYPE_A> inputsA, List<INPUT_TYPE_B> inputsB) {
        Gregmek.DEBUG_LOGGER.info("findFirstRecipe A:{} B:{}", inputsA.size(), inputsB.size());
        this.initCacheIfNeeded(world);
        Predicate<RECIPE> matchPredicate = (r) -> r.test(inputsA, inputsB);
        for (CACHE_A cacheA : cachesA) {
            for (INPUT_TYPE_A inputTypeA : inputsA) {
                RECIPE firstRecipe = cacheA.findFirstRecipe(inputTypeA, matchPredicate);
                if (firstRecipe != null) {
                    return firstRecipe;
                }
            }
        }

        return null;
    }

    protected void initCache(List<RECIPE> recipes) {
        Gregmek.DEBUG_LOGGER.info("InitCache recipe id: {} class: {}, type: {}, type object: {}, object: {}",
        recipeType.getRegistryName(),
        recipeType.getClass(),
        recipeType.getRecipeType(),
        Integer.toHexString(recipeType.getRecipeType().hashCode()),
        Integer.toHexString(recipeType.hashCode()));
        Gregmek.DEBUG_LOGGER.info("initCache recipes:{}", recipes.size());
        for(RECIPE recipe : recipes) {
            boolean complex = false;

            for (int i = 0; i < cachesA.size(); i++) {
                CACHE_A cacheA = cachesA.get(i);
                cacheA.mapInputs(recipe, getInputExtractor(inputsAExtractor, i).apply(recipe));
                complexIngredientsA.get(i).add(recipe);
                complex = true;
            }

            for (int i = 0; i < cachesB.size(); i++) {
                CACHE_B cacheB = cachesB.get(i);
                cacheB.mapInputs(recipe, getInputExtractor(inputsBExtractor, i).apply(recipe));
                complexIngredientsB.get(i).add(recipe);
                complex = true;
            }

            if (complex) {
                this.complexRecipes.add(recipe);
            }
        }

    }

    public record CacheExtractorMap<CACHE, RECIPE, INGREDIENT>(CACHE cache, Function<RECIPE, INGREDIENT> inputExtractor, Set<RECIPE> complexIngredient) {
    }
}