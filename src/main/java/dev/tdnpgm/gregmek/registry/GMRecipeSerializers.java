package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.recipes.AlloySmeltingRecipe;
import dev.tdnpgm.gregmek.recipes.AssemblingRecipe;
import dev.tdnpgm.gregmek.recipes.BendingRecipe;
import dev.tdnpgm.gregmek.recipes.impl.AlloySmeltingIRecipe;
import dev.tdnpgm.gregmek.recipes.impl.AssemblingIRecipe;
import dev.tdnpgm.gregmek.recipes.impl.BendingIRecipe;
import dev.tdnpgm.gregmek.recipes.serializer.AlloySmeltingRecipeSerializer;
import dev.tdnpgm.gregmek.recipes.serializer.AssemblingRecipeSerializer;
import dev.tdnpgm.gregmek.recipes.serializer.BendingRecipeSerializer;
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
