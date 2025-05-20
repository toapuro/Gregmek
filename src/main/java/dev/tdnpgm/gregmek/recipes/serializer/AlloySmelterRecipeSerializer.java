package dev.tdnpgm.gregmek.recipes.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.tdnpgm.gregmek.recipes.AlloySmelterRecipe;
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

public class AlloySmelterRecipeSerializer<RECIPE extends AlloySmelterRecipe> implements RecipeSerializer<RECIPE> {
    private final AlloySmelterRecipeSerializer.IFactory<RECIPE> factory;

    public AlloySmelterRecipeSerializer(AlloySmelterRecipeSerializer.IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    public @NotNull RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        JsonElement mainInput = GsonHelper.isArrayNode(json, "mainInput") ? GsonHelper.getAsJsonArray(json, "mainInput") : GsonHelper.getAsJsonObject(json, "mainInput");
        ItemStackIngredient mainIngredient = IngredientCreatorAccess.item().deserialize(mainInput);
        JsonElement secondaryInput = GsonHelper.isArrayNode(json, "secondaryInput") ? GsonHelper.getAsJsonArray(json, "secondaryInput") : GsonHelper.getAsJsonObject(json, "secondaryInput");
        ItemStackIngredient extraIngredient = IngredientCreatorAccess.item().deserialize(secondaryInput);
        ItemStack output = SerializerHelper.getItemStack(json, "output");
        if (output.isEmpty()) {
            throw new JsonSyntaxException("Combiner recipe output must not be empty.");
        } else {
            return this.factory.create(recipeId, mainIngredient, extraIngredient, output);
        }
    }

    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            ItemStackIngredient mainInput = IngredientCreatorAccess.item().read(buffer);
            ItemStackIngredient secondaryInput = IngredientCreatorAccess.item().read(buffer);
            ItemStack output = buffer.readItem();
            return this.factory.create(recipeId, mainInput, secondaryInput, output);
        } catch (Exception e) {
            Mekanism.logger.error("Error reading combiner recipe from packet.", e);
            throw e;
        }
    }

    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            Mekanism.logger.error("Error writing combiner recipe to packet.", e);
            throw e;
        }
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends AlloySmelterRecipe> {
        RECIPE create(ResourceLocation id, ItemStackIngredient mainInput, ItemStackIngredient secondaryInput, ItemStack output);
    }
}
