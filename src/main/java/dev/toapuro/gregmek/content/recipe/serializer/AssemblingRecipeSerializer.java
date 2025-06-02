package dev.toapuro.gregmek.content.recipe.serializer;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toapuro.gregmek.content.recipe.AssemblingRecipe;
import mekanism.api.SerializerHelper;
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
        JsonElement itemInputs = GsonHelper.isArrayNode(json, "itemInputs") ? GsonHelper.getAsJsonArray(json, "itemInputs") : GsonHelper.getAsJsonObject(json, "itemInputs");
        List<ItemStackIngredient> itemIngredients = new ArrayList<>();
        Iterable<JsonElement> jsonElements = itemInputs.isJsonArray() ? itemInputs.getAsJsonArray() : itemInputs.getAsJsonObject().asMap().values();
        for (JsonElement itemInput : jsonElements) {
            ItemStackIngredient solidIngredient = IngredientCreatorAccess.item().deserialize(itemInput);
            itemIngredients.add(solidIngredient);
        }

        List<FluidStackIngredient> fluidIngredients = new ArrayList<>();
        if (json.has("fluidInput")) {
            JsonElement fluidInput = GsonHelper.isArrayNode(json, "fluidInput") ? GsonHelper.getAsJsonArray(json, "fluidInput") : GsonHelper.getAsJsonObject(json, "fluidInput");
            if (fluidInput.isJsonArray() && !fluidInput.getAsJsonArray().isEmpty() ||
                    fluidInput.isJsonObject() && fluidInput.getAsJsonObject().size() != 0) {
                fluidIngredients.add(IngredientCreatorAccess.fluid().deserialize(fluidInput));
            }
        }

        JsonElement ticks = json.get("duration");
        if (!GsonHelper.isNumberValue(ticks)) {
            throw new JsonSyntaxException("Expected duration to be a number greater than zero.");
        }
        int duration = ticks.getAsJsonPrimitive().getAsInt();
        if (duration <= 0) {
            throw new JsonSyntaxException("Expected duration to be a number greater than zero.");
        }

        ItemStack itemOutput = ItemStack.EMPTY;
        if (json.has("itemOutput")) {
            itemOutput = SerializerHelper.getItemStack(json, "itemOutput");
            if (itemOutput.isEmpty()) {
                throw new JsonSyntaxException("Assembling item output must not be empty, if it is defined.");
            }

        }

        return this.factory.create(recipeId, itemIngredients, fluidIngredients, duration, itemOutput);
    }

    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            List<ItemStackIngredient> itemStackIngredients = new ArrayList<>();
            int inputSolidSize = buffer.readVarInt();
            for (int i = 0; i < inputSolidSize; i++) {
                ItemStackIngredient inputSolidIngredient = IngredientCreatorAccess.item().read(buffer);
                itemStackIngredients.add(inputSolidIngredient);
            }

            List<FluidStackIngredient> inputFluids = new ArrayList<>();
            int inputFluidSize = buffer.readVarInt();
            for (int i = 0; i < inputFluidSize; i++) {
                FluidStackIngredient inputFluid = IngredientCreatorAccess.fluid().read(buffer);
                inputFluids.add(inputFluid);
            }

            int duration = buffer.readVarInt();
            ItemStack outputItem = buffer.readItem();
            return this.factory.create(recipeId, itemStackIngredients, inputFluids, duration, outputItem);
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
        RECIPE create(ResourceLocation id, List<ItemStackIngredient> inputSolids, List<FluidStackIngredient> inputFluids, int duration, ItemStack outputItem);
    }
}