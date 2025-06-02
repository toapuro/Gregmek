package dev.tdnpgm.gregmek.content.recipe.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.tdnpgm.gregmek.content.recipe.AlloySmeltingRecipe;
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

public class AlloySmeltingRecipeSerializer<RECIPE extends AlloySmeltingRecipe> implements RecipeSerializer<RECIPE> {
    private final AlloySmeltingRecipeSerializer.IFactory<RECIPE> factory;

    public AlloySmeltingRecipeSerializer(AlloySmeltingRecipeSerializer.IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    public @NotNull RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        JsonElement mainInput = GsonHelper.isArrayNode(json, "mainInput") ? GsonHelper.getAsJsonArray(json, "mainInput") : GsonHelper.getAsJsonObject(json, "mainInput");
        ItemStackIngredient mainIngredient = IngredientCreatorAccess.item().deserialize(mainInput);
        JsonElement extraInput = GsonHelper.isArrayNode(json, "extraInput") ? GsonHelper.getAsJsonArray(json, "extraInput") : GsonHelper.getAsJsonObject(json, "extraInput");
        ItemStackIngredient secondaryIngredient = IngredientCreatorAccess.item().deserialize(extraInput);


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
            return this.factory.create(recipeId, mainIngredient, secondaryIngredient, duration, output);
        }
    }

    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            ItemStackIngredient mainIngredient = IngredientCreatorAccess.item().read(buffer);
            ItemStackIngredient secondaryIngredient = IngredientCreatorAccess.item().read(buffer);

            int duration = buffer.readVarInt();
            ItemStack output = buffer.readItem();
            return this.factory.create(recipeId, mainIngredient, secondaryIngredient, duration, output);
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
    public interface IFactory<RECIPE extends AlloySmeltingRecipe> {
        RECIPE create(ResourceLocation id, ItemStackIngredient mainInputSolid, ItemStackIngredient secondaryInputSolid, int duration, ItemStack output);
    }
}
