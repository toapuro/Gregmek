package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.recipes.AlloySmelterRecipe;
import dev.tdnpgm.gregmek.recipes.AssemblingRecipe;
import dev.tdnpgm.gregmek.recipes.impl.AlloySmelterIRecipe;
import dev.tdnpgm.gregmek.recipes.impl.AssemblingIRecipe;
import dev.tdnpgm.gregmek.recipes.serializer.AlloySmelterRecipeSerializer;
import dev.tdnpgm.gregmek.recipes.serializer.AssemblingRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class GMRecipeSerializers {
    public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(Gregmek.MODID);
    public static final RecipeSerializerRegistryObject<AssemblingRecipe> ASSEMBLING;
    public static final RecipeSerializerRegistryObject<AlloySmelterRecipe> ALLOY_SMELTER;

    static {
        ASSEMBLING = RECIPE_SERIALIZERS.register("assembling", () -> new AssemblingRecipeSerializer<>(AssemblingIRecipe::new));
        ALLOY_SMELTER = RECIPE_SERIALIZERS.register("alloy_smelter", () -> new AlloySmelterRecipeSerializer<>(AlloySmelterIRecipe::new));
    }
}
