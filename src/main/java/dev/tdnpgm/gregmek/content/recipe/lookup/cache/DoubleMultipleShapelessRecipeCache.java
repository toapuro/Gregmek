package dev.tdnpgm.gregmek.content.recipe.lookup.cache;

import dev.tdnpgm.gregmek.common.utils.GregmekUtils;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.AbstractInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.IInputCache;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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
        this.complexIngredientsA = GregmekUtils.makeListOf(HashSet::new, cachesA.size());
        this.complexIngredientsB = GregmekUtils.makeListOf(HashSet::new, cachesB.size());
        this.inputsAExtractor = inputsAExtractor;
        this.inputsBExtractor = inputsBExtractor;

        this.cachesA = cachesA;
        this.cachesB = cachesB;
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
        return containsInputs(world, inputs1, inputs1Extractor, caches1) ||
                containsInputs(world, inputs2, inputs2Extractor, caches2);
    }


    public <INPUT_TYPE, INGREDIENT_TYPE extends InputIngredient<INPUT_TYPE>,
            CACHE extends IInputCache<INPUT_TYPE, INGREDIENT_TYPE, RECIPE>> boolean containsInput(@Nullable Level world, INPUT_TYPE input, Function<RECIPE, List<INGREDIENT_TYPE>> inputsExtractor, List<CACHE> caches) {
        for (int i = 0; i < caches.size(); i++) {
            CACHE cache = caches.get(i);
            if (containsInput(world, input, inputsExtractor, i, cache, complexRecipes)) {
                return true;
            }
        }

        return false;
    }

    protected <INPUT, INGREDIENT extends InputIngredient<INPUT>, CACHE extends IInputCache<INPUT, INGREDIENT, RECIPE>> boolean containsInput(@Nullable Level world, INPUT input, Function<RECIPE, List<INGREDIENT>> inputsExtractor, int ingredientIndex, CACHE cache, Set<RECIPE> complexRecipes)  {
        if (cache.isEmpty(input)) {
            return false;
        } else {
            this.initCacheIfNeeded(world);
            return cache.contains(input) || complexRecipes.stream().anyMatch((recipe) -> {
                List<INGREDIENT> ingredients = inputsExtractor.apply(recipe);
                if(ingredients.size() <= ingredientIndex) {
                    return false;
                }

                INGREDIENT ingredient = ingredients.get(ingredientIndex);

                return ingredient.testType(input);
            });
        }
    }

    public <INPUT_TYPE, INGREDIENT_TYPE extends InputIngredient<INPUT_TYPE>,
            CACHE extends IInputCache<INPUT_TYPE, INGREDIENT_TYPE, RECIPE>> boolean containsInputs(@Nullable Level world, List<INPUT_TYPE> inputs, Function<RECIPE, List<INGREDIENT_TYPE>> inputsExtractor, List<CACHE> caches) {
        this.initCacheIfNeeded(world);

        for (INPUT_TYPE input : inputs) {
            int matched = 0;

            for (int i = 0; i < caches.size(); i++) {
                CACHE cache = caches.get(i);
                if(containsInput(world, input, inputsExtractor, i, cache, complexRecipes)) {
                    matched++;
                    break;
                }
            }

            if(matched == 0) {
                return false;
            }
        }

        return true;
    }

    public @Nullable RECIPE findFirstRecipe(@Nullable Level world, List<INPUT_TYPE_A> inputsA, List<INPUT_TYPE_B> inputsB) {
        this.initCacheIfNeeded(world);

        Predicate<RECIPE> matchPredicate = (r) -> r.test(inputsA, inputsB);
        for (INPUT_TYPE_A inputTypeA : inputsA) {
            for (CACHE_A cacheA : cachesA) {
                RECIPE firstRecipe = cacheA.findFirstRecipe(inputTypeA, matchPredicate);
                if (firstRecipe != null) {
                    return firstRecipe;
                }
            }
        }

        return null;
    }

    protected void initCache(List<RECIPE> recipes) {
        for(RECIPE recipe : recipes) {
            boolean complex = false;
            for (int i = 0; i < inputsAExtractor.apply(recipe).size(); i++) {
                CACHE_A cacheA = cachesA.get(i);
                cacheA.mapInputs(recipe, getInputExtractor(inputsAExtractor, i).apply(recipe));
                complexIngredientsA.get(i).add(recipe);
                complex = true;
            }

            for (int i = 0; i < inputsBExtractor.apply(recipe).size(); i++) {
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
}