package dev.tdnpgm.gregmek.recipes.lookup.cache;

import dev.tdnpgm.gregmek.utils.GregmekUtils;
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
import java.util.function.Function;
import java.util.function.Predicate;

public class SingleMultipleShapelessRecipeCache<
        INPUT_TYPE, INGREDIENT_TYPE extends InputIngredient<INPUT_TYPE>,
        RECIPE extends MekanismRecipe & Predicate<List<INPUT_TYPE>>,
        CACHE
                extends IInputCache<INPUT_TYPE, INGREDIENT_TYPE, RECIPE>> extends AbstractInputRecipeCache<RECIPE> {
    private final List<Set<RECIPE>> complexIngredients;
    private final Set<RECIPE> complexRecipes = new HashSet<>();
    private final Function<RECIPE, List<INGREDIENT_TYPE>> inputsExtractor;
    private final List<CACHE> caches;

    protected SingleMultipleShapelessRecipeCache(MekanismRecipeType<RECIPE, ?> recipeType, Function<RECIPE, List<INGREDIENT_TYPE>> inputsExtractor, List<CACHE> caches) {
        super(recipeType);
        this.complexIngredients = GregmekUtils.makeListOf(HashSet::new, caches.size());
        this.inputsExtractor = inputsExtractor;

        this.caches = caches;
    }

    public void clear() {
        super.clear();
        this.caches.forEach(IInputCache::clear);
        this.complexIngredients.forEach(Set::clear);
        this.complexRecipes.clear();
    }

    public boolean containsInputs(@Nullable Level world, List<INPUT_TYPE> inputs) {
        return this.containsInputs(world, inputs, inputsExtractor, caches);
    }

    public boolean containsInput(@Nullable Level world, INPUT_TYPE input) {
        return this.containsInput(world, input, inputsExtractor, caches);
    }

    public <INGREDIENT> Function<RECIPE, INGREDIENT> getInputExtractor(Function<RECIPE, List<INGREDIENT>> inputsExtractor, int index) {
        return recipe -> inputsExtractor.apply(recipe).get(index);
    }

    public <INPUT1, INGREDIENT1 extends InputIngredient<INPUT1>,
            CACHE1 extends IInputCache<INPUT1, INGREDIENT1, RECIPE>> boolean containsInput(@Nullable Level world, INPUT1 input1, Function<RECIPE, List<INGREDIENT1>> inputsExtractor, List<CACHE1> caches) {
        for (int i = 0; i < caches.size(); i++) {
            CACHE1 CACHE1 = caches.get(i);
            if (containsInput(world, input1, inputsExtractor, i, CACHE1, complexRecipes)) {
                return true;
            }
        }

        return false;
    }

    protected <INPUT_TYPE1, INGREDIENT_TYPE1 extends InputIngredient<INPUT_TYPE1>, CACHE1 extends IInputCache<INPUT_TYPE1, INGREDIENT_TYPE1, RECIPE>> boolean containsInput(@Nullable Level world, INPUT_TYPE1 input1, Function<RECIPE, List<INGREDIENT_TYPE1>> inputsExtractor, int ingredientIndex, CACHE1 cache1, Set<RECIPE> complexRecipes) {
        if (cache1.isEmpty(input1)) {
            return false;
        } else {
            this.initCacheIfNeeded(world);
            return cache1.contains(input1) || complexRecipes.stream().anyMatch((recipe) -> {
                List<INGREDIENT_TYPE1> ingredients = inputsExtractor.apply(recipe);
                if (ingredients.size() <= ingredientIndex) {
                    return false;
                }

                INGREDIENT_TYPE1 ingredient = ingredients.get(ingredientIndex);

                return ingredient.testType(input1);
            });
        }
    }

    public <INPUT_TYPE1, INGREDIENT_TYPE1 extends InputIngredient<INPUT_TYPE1>,
            CACHE1 extends IInputCache<INPUT_TYPE1, INGREDIENT_TYPE1, RECIPE>> boolean containsInputs(@Nullable Level world, List<INPUT_TYPE1> inputs, Function<RECIPE, List<INGREDIENT_TYPE1>> inputsExtractor, List<CACHE1> caches) {
        this.initCacheIfNeeded(world);

        for (INPUT_TYPE1 input : inputs) {
            int matched = 0;

            for (int i = 0; i < caches.size(); i++) {
                CACHE1 cache = caches.get(i);
                if (containsInput(world, input, inputsExtractor, i, cache, complexRecipes)) {
                    matched++;
                    break;
                }
            }

            if (matched == 0) {
                return false;
            }
        }

        return true;
    }

    public @Nullable RECIPE findFirstRecipe(@Nullable Level world, List<INPUT_TYPE> inputsA) {
        this.initCacheIfNeeded(world);

        Predicate<RECIPE> matchPredicate = (r) -> r.test(inputsA);
        for (INPUT_TYPE inputTypeA : inputsA) {
            for (CACHE cacheA : caches) {
                RECIPE firstRecipe = cacheA.findFirstRecipe(inputTypeA, matchPredicate);
                if (firstRecipe != null) {
                    return firstRecipe;
                }
            }
        }

        return null;
    }

    protected void initCache(List<RECIPE> recipes) {
        for (RECIPE recipe : recipes) {
            boolean complex = false;
            for (int i = 0; i < inputsExtractor.apply(recipe).size(); i++) {
                CACHE cacheA = caches.get(i);
                cacheA.mapInputs(recipe, getInputExtractor(inputsExtractor, i).apply(recipe));
                complexIngredients.get(i).add(recipe);
                complex = true;
            }

            if (complex) {
                this.complexRecipes.add(recipe);
            }
        }
    }
}