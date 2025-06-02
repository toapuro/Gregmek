package dev.toapuro.gregmek.content.registry;

import dev.toapuro.gregmek.Gregmek;
import dev.toapuro.gregmek.content.recipe.AlloySmeltingRecipe;
import dev.toapuro.gregmek.content.recipe.AssemblingRecipe;
import dev.toapuro.gregmek.content.recipe.BendingRecipe;
import dev.toapuro.gregmek.content.recipe.impl.AlloySmeltingIRecipe;
import dev.toapuro.gregmek.content.recipe.impl.AssemblingIRecipe;
import dev.toapuro.gregmek.content.recipe.impl.BendingIRecipe;
import dev.toapuro.gregmek.content.recipe.serializer.AlloySmeltingRecipeSerializer;
import dev.toapuro.gregmek.content.recipe.serializer.AssemblingRecipeSerializer;
import dev.toapuro.gregmek.content.recipe.serializer.BendingRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class GMRecipeSerializers {
    public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(Gregmek.MODID);
    public static final RecipeSerializerRegistryObject<AssemblingRecipe> ASSEMBLING;
    public static final RecipeSerializerRegistryObject<AlloySmeltingRecipe> ALLOY_SMELTING;
    public static final RecipeSerializerRegistryObject<BendingRecipe> BENDING;

    static {
        ASSEMBLING = RECIPE_SERIALIZERS.register("assembling", () -> new AssemblingRecipeSerializer<>(AssemblingIRecipe::new));
        ALLOY_SMELTING = RECIPE_SERIALIZERS.register("alloy_smelting", () -> new AlloySmeltingRecipeSerializer<>(AlloySmeltingIRecipe::new));
        BENDING = RECIPE_SERIALIZERS.register("bending", () -> new BendingRecipeSerializer<>(BendingIRecipe::new));
    }
}
