package dev.toapuro.gregmek.content.recipe.serializer;

import dev.toapuro.gregmek.content.recipe.AlloySmeltingRecipe;
import dev.toapuro.gregmek.content.recipe.AssemblingRecipe;
import dev.toapuro.gregmek.content.recipe.BendingRecipe;
import dev.toapuro.gregmek.content.recipe.impl.AlloySmeltingIRecipe;
import dev.toapuro.gregmek.content.recipe.impl.AssemblingIRecipe;
import dev.toapuro.gregmek.content.recipe.impl.BendingIRecipe;
import dev.toapuro.gregmek.content.recipe.serializer.builder.RecipeSerializerFactory;
import dev.toapuro.gregmek.content.recipe.serializer.builder.SerializerBuilder;
import dev.toapuro.gregmek.content.recipe.serializer.field.SerializerFieldTypes;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class GMSerializerTypes {
    public static RecipeSerializer<AssemblingRecipe> ASSEMBLING;
    public static RecipeSerializer<AlloySmeltingRecipe> ALLOY_SMELTING;
    public static RecipeSerializer<BendingRecipe> BENDING;

    static {
        ASSEMBLING = RecipeSerializerFactory.composeSerializer(() -> {
            var itemInputs = SerializerFieldTypes.ITEM_INGREDIENTS.define("itemInputs");
            var fluidInputs = SerializerFieldTypes.FLUID_INGREDIENTS.define("fluidInputs");
            var duration = SerializerFieldTypes.DURATION.define("duration");
            var itemOutput = SerializerFieldTypes.ITEM_STACK.define("itemOutput");

            return SerializerBuilder.create(AssemblingRecipe.class)
                    .bind(itemInputs, AssemblingRecipe::getInputSolids)
                    .bind(fluidInputs, AssemblingRecipe::getInputFluids)
                    .bind(duration, AssemblingRecipe::getDuration)
                    .bind(itemOutput, AssemblingRecipe::getOutput)
                    .build((resourceLocation) ->
                            new AssemblingIRecipe(resourceLocation, itemInputs.get(), fluidInputs.get(), duration.get(), itemOutput.get()));
        });

        ALLOY_SMELTING = RecipeSerializerFactory.composeSerializer(() -> {
            var mainInput = SerializerFieldTypes.ITEM_INGREDIENT.define("mainInput");
            var extraInput = SerializerFieldTypes.ITEM_INGREDIENT.define("extraInput");
            var duration = SerializerFieldTypes.DURATION.define("duration");
            var itemOutput = SerializerFieldTypes.ITEM_STACK.define("itemOutput");

            return SerializerBuilder.create(AlloySmeltingRecipe.class)
                    .bind(mainInput, AlloySmeltingRecipe::getMainInput)
                    .bind(extraInput, AlloySmeltingRecipe::getSecondaryInput)
                    .bind(duration, AlloySmeltingRecipe::getDuration)
                    .bind(itemOutput, AlloySmeltingRecipe::getOutput)
                    .build((resourceLocation) ->
                            new AlloySmeltingIRecipe(resourceLocation, mainInput.get(), extraInput.get(), duration.get(), itemOutput.get()));
        });

        BENDING = RecipeSerializerFactory.composeSerializer(() -> {
            var itemInputs = SerializerFieldTypes.ITEM_INGREDIENTS.define("itemInputs");
            var duration = SerializerFieldTypes.DURATION.define("duration");
            var itemOutput = SerializerFieldTypes.ITEM_STACK.define("itemOutput");

            return SerializerBuilder.create(BendingRecipe.class)
                    .bind(itemInputs, BendingRecipe::getInputSolids)
                    .bind(duration, BendingRecipe::getDuration)
                    .bind(itemOutput, BendingRecipe::getOutput)
                    .build((resourceLocation) ->
                            new BendingIRecipe(resourceLocation, itemInputs.get(), duration.get(), itemOutput.get()));
        });
    }
}
