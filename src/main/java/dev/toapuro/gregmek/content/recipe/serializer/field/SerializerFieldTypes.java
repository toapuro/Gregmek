package dev.toapuro.gregmek.content.recipe.serializer.field;

import com.google.gson.JsonObject;
import dev.toapuro.gregmek.common.helper.BufferHelper;
import dev.toapuro.gregmek.common.helper.RecipeJsonHelper;
import dev.toapuro.gregmek.content.tier.GMTier;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SerializerFieldTypes {
    public static SerializerFieldType<ItemStackIngredient> ITEM_INGREDIENT;
    public static SerializerFieldType<List<ItemStackIngredient>> ITEM_INGREDIENTS;
    public static SerializerFieldType<FluidStackIngredient> FLUID_INGREDIENT;
    public static SerializerFieldType<List<FluidStackIngredient>> FLUID_INGREDIENTS;
    public static SerializerFieldType<Integer> DURATION;
    public static SerializerFieldType<ItemStack> ITEM_STACK;
    public static SerializerFieldType<GMTier> TIER;

    static {
        ITEM_INGREDIENT = SerializerFieldType.create((fieldName) -> new AbstractSerializerField<ItemStackIngredient>() {
            @Override
            public void readRecipe(JsonObject jsonObject) {
                value = RecipeJsonHelper.deserializeIngredient(jsonObject, fieldName,
                        jsonElement -> IngredientCreatorAccess.item().deserialize(jsonElement));
            }

            @Override
            public void readFromBuffer(FriendlyByteBuf byteBuf) {
                value = IngredientCreatorAccess.item().read(byteBuf);
            }

            @Override
            public void writeToBuffer(FriendlyByteBuf byteBuf) {
                value.write(byteBuf);
            }
        });

        ITEM_INGREDIENTS = SerializerFieldType.create((fieldName) -> new AbstractSerializerField<List<ItemStackIngredient>>() {
            @Override
            public void readRecipe(JsonObject jsonObject) {
                value = RecipeJsonHelper.deserializeItemIngredients(jsonObject, fieldName);
            }

            @Override
            public void readFromBuffer(FriendlyByteBuf byteBuf) {
                value = BufferHelper.readList(byteBuf, i ->
                        IngredientCreatorAccess.item().read(byteBuf));
            }

            @Override
            public void writeToBuffer(FriendlyByteBuf byteBuf) {
                BufferHelper.writeList(byteBuf, value, ingredient ->
                        ingredient.write(byteBuf));
            }
        });
        FLUID_INGREDIENT = SerializerFieldType.create((fieldName) -> new AbstractSerializerField<FluidStackIngredient>() {
            @Override
            public void readRecipe(JsonObject jsonObject) {
                value = RecipeJsonHelper.deserializeIngredient(jsonObject, fieldName,
                        jsonElement -> IngredientCreatorAccess.fluid().deserialize(jsonElement));
            }

            @Override
            public void readFromBuffer(FriendlyByteBuf byteBuf) {
                value = IngredientCreatorAccess.fluid().read(byteBuf);
            }

            @Override
            public void writeToBuffer(FriendlyByteBuf byteBuf) {
                value.write(byteBuf);
            }
        });

        FLUID_INGREDIENTS = SerializerFieldType.create((fieldName) -> new AbstractSerializerField<List<FluidStackIngredient>>() {
            @Override
            public void readRecipe(JsonObject jsonObject) {
                value = RecipeJsonHelper.deserializeFluidIngredients(jsonObject, fieldName);
            }

            @Override
            public void readFromBuffer(FriendlyByteBuf byteBuf) {
                value = BufferHelper.readList(byteBuf, i ->
                        IngredientCreatorAccess.fluid().read(byteBuf));
            }

            @Override
            public void writeToBuffer(FriendlyByteBuf byteBuf) {
                BufferHelper.writeList(byteBuf, value, ingredient ->
                        ingredient.write(byteBuf));
            }
        });

        DURATION = SerializerFieldType.create((fieldName) -> new AbstractSerializerField<Integer>() {
            @Override
            public void readRecipe(JsonObject jsonObject) {
                value = RecipeJsonHelper.validateDuration(jsonObject, "duration");
            }

            @Override
            public void readFromBuffer(FriendlyByteBuf byteBuf) {
                value = byteBuf.readVarInt();
            }

            @Override
            public void writeToBuffer(FriendlyByteBuf byteBuf) {
                byteBuf.writeVarInt(value);
            }
        });

        ITEM_STACK = SerializerFieldType.create((fieldName) -> new AbstractSerializerField<ItemStack>() {
            @Override
            public void readRecipe(JsonObject jsonObject) {
                value = SerializerHelper.getItemStack(jsonObject, fieldName);
            }

            @Override
            public void readFromBuffer(FriendlyByteBuf byteBuf) {
                value = byteBuf.readItem();
            }

            @Override
            public void writeToBuffer(FriendlyByteBuf byteBuf) {
                byteBuf.writeItem(value);
            }
        });

        TIER = SerializerFieldType.create((fieldName) -> new AbstractSerializerField<GMTier>() {
            @Override
            public void readRecipe(JsonObject jsonObject) {
                value = RecipeJsonHelper.deserializeEnum(jsonObject, fieldName, GMTier.values());
            }

            @Override
            public void readFromBuffer(FriendlyByteBuf byteBuf) {
                value = GMTier.values()[byteBuf.readVarInt()];
            }

            @Override
            public void writeToBuffer(FriendlyByteBuf byteBuf) {
                byteBuf.writeVarInt(value.ordinal());
            }
        });
    }
}
