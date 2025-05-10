package dev.tdnpgm.gregmek.recipes;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.tdnpgm.gregmek.Gregmek;
import mekanism.api.SerializerHelper;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AssemblingRecipeSerializer<RECIPE extends AssemblingRecipe> implements RecipeSerializer<RECIPE> {
    private final IFactory<RECIPE> factory;

    public AssemblingRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    public @NotNull RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        JsonElement itemInputs = (JsonElement)(GsonHelper.isArrayNode(json, "itemInputs") ? GsonHelper.getAsJsonArray(json, "itemInputs") : GsonHelper.getAsJsonObject(json, "itemInputs"));

        Gregmek.DEBUG_LOGGER.info("Registering Assembling Recipe JSON: {}", json.toString());

        List<ItemStackIngredient> ingredients = new ArrayList<>();
        Iterable<JsonElement> jsonElements = itemInputs.isJsonArray() ? itemInputs.getAsJsonArray() : itemInputs.getAsJsonObject().asMap().values();
        for (JsonElement itemInput : jsonElements) {
            ItemStackIngredient solidIngredient = (ItemStackIngredient)IngredientCreatorAccess.item().deserialize(itemInput);
            ingredients.add(solidIngredient);
            Gregmek.DEBUG_LOGGER.info("Ingredient: {}", solidIngredient.toString());
        }

        JsonElement fluidInput = (JsonElement)(GsonHelper.isArrayNode(json, "fluidInput") ? GsonHelper.getAsJsonArray(json, "fluidInput") : GsonHelper.getAsJsonObject(json, "fluidInput"));
        FluidStackIngredient fluidIngredient = (FluidStackIngredient)IngredientCreatorAccess.fluid().deserialize(fluidInput);
        FloatingLong energyRequired = FloatingLong.ZERO;
        if (json.has("energyRequired")) {
            energyRequired = SerializerHelper.getFloatingLong(json, "energyRequired");
        }

        JsonElement ticks = json.get("duration");
        if (!GsonHelper.isNumberValue(ticks)) {
            throw new JsonSyntaxException("Expected duration to be a number greater than zero.");
        } else {
            int duration = ticks.getAsJsonPrimitive().getAsInt();
            if (duration <= 0) {
                throw new JsonSyntaxException("Expected duration to be a number greater than zero.");
            } else {
                ItemStack itemOutput = ItemStack.EMPTY;
                if (json.has("itemOutput")) {
                    itemOutput = SerializerHelper.getItemStack(json, "itemOutput");
                    if (itemOutput.isEmpty()) {
                        throw new JsonSyntaxException("Assembling item output must not be empty, if it is defined.");
                    }

                }

                return this.factory.create(recipeId, ingredients, fluidIngredient, energyRequired, duration, itemOutput);
            }
        }
    }

    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            List<ItemStackIngredient> itemStackIngredients = new ArrayList<>();
            int inputSolidSize = buffer.readInt();
            for (int i = 0; i < inputSolidSize; i++) {
                ItemStackIngredient inputSolidIngredient = IngredientCreatorAccess.item().read(buffer);
                itemStackIngredients.add(inputSolidIngredient);

            }
            FluidStackIngredient inputFluid = IngredientCreatorAccess.fluid().read(buffer);

            FloatingLong energyRequired = FloatingLong.readFromBuffer(buffer);
            int duration = buffer.readVarInt();
            ItemStack outputItem = buffer.readItem();
            return this.factory.create(recipeId, itemStackIngredients, inputFluid, energyRequired, duration, outputItem);
        } catch (Exception e) {
            Mekanism.logger.error("Error reading assembling recipe from packet.", e);
            throw e;
        }
    }

    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            Mekanism.logger.error("Error writing assembling recipe to packet.", e);
            throw e;
        }
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends AssemblingRecipe> {
        RECIPE create(ResourceLocation id, List<ItemStackIngredient> inputSolids, FluidStackIngredient inputFluid, FloatingLong energyRequired, int duration, ItemStack outputItem);
    }
}