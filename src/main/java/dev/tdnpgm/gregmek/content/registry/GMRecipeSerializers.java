package dev.tdnpgm.gregmek.content.registry;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.content.recipe.AlloySmeltingRecipe;
import dev.tdnpgm.gregmek.content.recipe.AssemblingRecipe;
import dev.tdnpgm.gregmek.content.recipe.BendingRecipe;
import dev.tdnpgm.gregmek.content.recipe.impl.AlloySmeltingIRecipe;
import dev.tdnpgm.gregmek.content.recipe.impl.AssemblingIRecipe;
import dev.tdnpgm.gregmek.content.recipe.impl.BendingIRecipe;
import dev.tdnpgm.gregmek.content.recipe.serializer.AlloySmeltingRecipeSerializer;
import dev.tdnpgm.gregmek.content.recipe.serializer.AssemblingRecipeSerializer;
import dev.tdnpgm.gregmek.content.recipe.serializer.BendingRecipeSerializer;
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
