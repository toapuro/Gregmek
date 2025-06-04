package dev.toapuro.gregmek.common.helper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toapuro.gregmek.common.api.exceptions.GMRecipeError;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.util.GsonHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class RecipeJsonHelper {
    public static int validateDuration(JsonObject json, String field) {
        JsonElement ticks = json.get(field);

        if (!GsonHelper.isNumberValue(ticks)) {
            throw GMRecipeError.DURATION_NOT_NUMBER;
        }
        int duration = ticks.getAsInt();
        if (duration <= 0) {
            throw GMRecipeError.DURATION_NOT_NUMBER;
        }
        return duration;
    }

    public static <ENUM extends Enum<?>> ENUM deserializeEnum(JsonObject json, String field, ENUM[] values) {
        JsonElement enumElement = json.get(field);

        if (GsonHelper.isNumberValue(enumElement)) {
            int ordinal = enumElement.getAsInt();
            if (ordinal <= 0) {
                throw GMRecipeError.ENUM_ORDINAL_NOT_POSITIVE;
            }
            if (ordinal >= values.length) {
                throw GMRecipeError.ENUM_ORDINAL_OUT_RANGE;
            }
            return values[ordinal];
        } else if (GsonHelper.isStringValue(enumElement)) {
            String string = enumElement.getAsString();
            Optional<ENUM> first = Arrays.stream(values).filter(anEnum -> anEnum.name().equals(string)).findFirst();
            if (first.isEmpty()) {
                throw GMRecipeError.ENUM_UNKNOWN;
            }
            return first.get();
        } else {
            throw GMRecipeError.UNKNOWN_TYPE;
        }
    }

    public static <INGREDIENT> INGREDIENT deserializeIngredient(JsonObject json, String field, Function<JsonElement, INGREDIENT> deserializer) {
        JsonElement jsonElement = GsonHelper.isArrayNode(json, field) ? GsonHelper.getAsJsonArray(json, field) : GsonHelper.getAsJsonObject(json, field);
        return deserializer.apply(jsonElement);
    }

    public static <INGREDIENT> List<INGREDIENT> deserializeIngredientArray(JsonObject json, String field, Function<JsonElement, INGREDIENT> deserializer) {
        JsonElement jsonElement = GsonHelper.isArrayNode(json, field) ? GsonHelper.getAsJsonArray(json, field) : GsonHelper.getAsJsonObject(json, field);
        List<INGREDIENT> ingredients = new ArrayList<>();
        Iterable<JsonElement> elements = jsonElement.isJsonArray() ? jsonElement.getAsJsonArray() : jsonElement.getAsJsonObject().asMap().values();
        for (JsonElement element : elements) {
            ingredients.add(deserializer.apply(element));
        }
        return ingredients;
    }

    public static ItemStackIngredient deserializeItemIngredient(JsonObject json, String field) {
        JsonElement jsonElement = GsonHelper.isArrayNode(json, field) ? GsonHelper.getAsJsonArray(json, field) : GsonHelper.getAsJsonObject(json, field);
        return IngredientCreatorAccess.item().deserialize(jsonElement);
    }

    public static List<ItemStackIngredient> deserializeItemIngredients(JsonObject json, String field) {
        JsonElement itemInputs = GsonHelper.isArrayNode(json, field) ? GsonHelper.getAsJsonArray(json, field) : GsonHelper.getAsJsonObject(json, field);
        List<ItemStackIngredient> itemIngredients = new ArrayList<>();
        Iterable<JsonElement> jsonElements = itemInputs.isJsonArray() ? itemInputs.getAsJsonArray() : itemInputs.getAsJsonObject().asMap().values();
        for (JsonElement itemInput : jsonElements) {
            ItemStackIngredient ingredient = IngredientCreatorAccess.item().deserialize(itemInput);
            itemIngredients.add(ingredient);
        }
        return itemIngredients;
    }

    public static FluidStackIngredient deserializeFluidIngredient(JsonObject json, String field) {
        JsonElement jsonElement = GsonHelper.isArrayNode(json, field) ? GsonHelper.getAsJsonArray(json, field) : GsonHelper.getAsJsonObject(json, field);
        return IngredientCreatorAccess.fluid().deserialize(jsonElement);
    }

    public static List<FluidStackIngredient> deserializeFluidIngredients(JsonObject json, String field) {
        JsonElement fluidInputs = GsonHelper.isArrayNode(json, field) ? GsonHelper.getAsJsonArray(json, field) : GsonHelper.getAsJsonObject(json, field);
        List<FluidStackIngredient> fluidIngredients = new ArrayList<>();
        Iterable<JsonElement> jsonElements = fluidInputs.isJsonArray() ? fluidInputs.getAsJsonArray() : fluidInputs.getAsJsonObject().asMap().values();
        for (JsonElement fluidInput : jsonElements) {
            FluidStackIngredient ingredient = IngredientCreatorAccess.fluid().deserialize(fluidInput);
            fluidIngredients.add(ingredient);
        }
        return fluidIngredients;
    }
}
