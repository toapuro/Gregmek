package dev.toapuro.gregmek.content.registry;

import dev.toapuro.gregmek.Gregmek;
import dev.toapuro.gregmek.content.recipe.AlloySmeltingRecipe;
import dev.toapuro.gregmek.content.recipe.AssemblingRecipe;
import dev.toapuro.gregmek.content.recipe.BendingRecipe;
import dev.toapuro.gregmek.content.recipe.serializer.GMSerializerTypes;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class GMRecipeSerializers {
    public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(Gregmek.MODID);
    public static final RecipeSerializerRegistryObject<AssemblingRecipe> ASSEMBLING;
    public static final RecipeSerializerRegistryObject<AlloySmeltingRecipe> ALLOY_SMELTING;
    public static final RecipeSerializerRegistryObject<BendingRecipe> BENDING;

    static {
        ASSEMBLING = RECIPE_SERIALIZERS.register("assembling", () -> GMSerializerTypes.ASSEMBLING);
        ALLOY_SMELTING = RECIPE_SERIALIZERS.register("alloy_smelting", () -> GMSerializerTypes.ALLOY_SMELTING);
        BENDING = RECIPE_SERIALIZERS.register("bending", () -> GMSerializerTypes.BENDING);
    }
}
