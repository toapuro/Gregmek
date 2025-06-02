package dev.tdnpgm.gregmek.content.recipe.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.tdnpgm.gregmek.content.recipe.BendingRecipe;
import mekanism.api.SerializerHelper;
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

public class BendingRecipeSerializer<RECIPE extends BendingRecipe> implements RecipeSerializer<RECIPE> {
    private final BendingRecipeSerializer.IFactory<RECIPE> factory;

    public BendingRecipeSerializer(BendingRecipeSerializer.IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    public @NotNull RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        JsonElement itemInputs = GsonHelper.isArrayNode(json, "itemInputs") ? GsonHelper.getAsJsonArray(json, "itemInputs") : GsonHelper.getAsJsonObject(json, "itemInputs");
        List<ItemStackIngredient> itemIngredients = new ArrayList<>();
        Iterable<JsonElement> jsonElements = itemInputs.isJsonArray() ? itemInputs.getAsJsonArray() : itemInputs.getAsJsonObject().asMap().values();
        for (JsonElement itemInput : jsonElements) {
            ItemStackIngredient solidIngredient = IngredientCreatorAccess.item().deserialize(itemInput);
            itemIngredients.add(solidIngredient);
        }

        JsonElement ticks = json.get("duration");
        if (!GsonHelper.isNumberValue(ticks)) {
            throw new JsonSyntaxException("Expected duration to be a number greater than zero.");
        }
        int duration = ticks.getAsJsonPrimitive().getAsInt();
        if (duration <= 0) {
            throw new JsonSyntaxException("Expected duration to be a number greater than zero.");
        }

        ItemStack output = SerializerHelper.getItemStack(json, "itemOutput");
        if (output.isEmpty()) {
            throw new JsonSyntaxException("Alloy smelter recipe output must not be empty.");
        } else {
            return this.factory.create(recipeId, itemIngredients, duration, output);
        }
    }

    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            List<ItemStackIngredient> itemStackIngredients = new ArrayList<>();
            int inputSolidSize = buffer.readVarInt();
            for (int i = 0; i < inputSolidSize; i++) {
                ItemStackIngredient inputSolidIngredient = IngredientCreatorAccess.item().read(buffer);
                itemStackIngredients.add(inputSolidIngredient);
            }

            int duration = buffer.readVarInt();
            ItemStack output = buffer.readItem();
            return this.factory.create(recipeId, itemStackIngredients, duration, output);
        } catch (Exception e) {
            Mekanism.logger.error("Error reading alloy smelter recipe from packet.", e);
            throw e;
        }
    }

    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            Mekanism.logger.error("Error writing alloy smelter recipe to packet.", e);
            throw e;
        }
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends BendingRecipe> {
        RECIPE create(ResourceLocation id, List<ItemStackIngredient> inputs, int duration, ItemStack output);
    }
}
