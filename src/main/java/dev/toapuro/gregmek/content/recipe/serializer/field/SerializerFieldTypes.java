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
    public static final SerializerFieldType<ItemStackIngredient> ITEM_INGREDIENT;
    public static final SerializerFieldType<List<ItemStackIngredient>> ITEM_INGREDIENTS;
    public static final SerializerFieldType<FluidStackIngredient> FLUID_INGREDIENT;
    public static final SerializerFieldType<List<FluidStackIngredient>> FLUID_INGREDIENTS;
    public static final SerializerFieldType<Integer> DURATION;
    public static final SerializerFieldType<ItemStack> ITEM_STACK;
    public static final SerializerFieldType<GMTier> TIER;

    static {
        ITEM_INGREDIENT = SerializerFieldType.create((fieldName) -> new AbstractSerializerField<ItemStackIngredient>() {
            @Override
            public ItemStackIngredient readJsonRecipe(JsonObject jsonObject) {
                return RecipeJsonHelper.deserializeIngredient(jsonObject, fieldName,
                        jsonElement -> IngredientCreatorAccess.item().deserialize(jsonElement));
            }

            @Override
            public ItemStackIngredient readBuffer(FriendlyByteBuf byteBuf) {
                return IngredientCreatorAccess.item().read(byteBuf);
            }

            @Override
            public void writeToBuffer(FriendlyByteBuf byteBuf) {
                value.write(byteBuf);
            }
        });

        ITEM_INGREDIENTS = SerializerFieldType.create((fieldName) -> new AbstractSerializerField<List<ItemStackIngredient>>() {
            @Override
            public List<ItemStackIngredient> readJsonRecipe(JsonObject jsonObject) {
                return RecipeJsonHelper.deserializeItemIngredients(jsonObject, fieldName);
            }

            @Override
            public List<ItemStackIngredient> readBuffer(FriendlyByteBuf byteBuf) {
                return BufferHelper.readList(byteBuf, i ->
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
            public FluidStackIngredient readJsonRecipe(JsonObject jsonObject) {
                return RecipeJsonHelper.deserializeIngredient(jsonObject, fieldName,
                        jsonElement -> IngredientCreatorAccess.fluid().deserialize(jsonElement));
            }

            @Override
            public FluidStackIngredient readBuffer(FriendlyByteBuf byteBuf) {
                return IngredientCreatorAccess.fluid().read(byteBuf);
            }

            @Override
            public void writeToBuffer(FriendlyByteBuf byteBuf) {
                value.write(byteBuf);
            }
        });

        FLUID_INGREDIENTS = SerializerFieldType.create((fieldName) -> new AbstractSerializerField<List<FluidStackIngredient>>() {
            @Override
            public List<FluidStackIngredient> readJsonRecipe(JsonObject jsonObject) {
                return RecipeJsonHelper.deserializeFluidIngredients(jsonObject, fieldName);
            }

            @Override
            public List<FluidStackIngredient> readBuffer(FriendlyByteBuf byteBuf) {
                return BufferHelper.readList(byteBuf, i ->
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
            public Integer readJsonRecipe(JsonObject jsonObject) {
                return RecipeJsonHelper.validateDuration(jsonObject, "duration");
            }

            @Override
            public Integer readBuffer(FriendlyByteBuf byteBuf) {
                return byteBuf.readVarInt();
            }

            @Override
            public void writeToBuffer(FriendlyByteBuf byteBuf) {
                byteBuf.writeVarInt(value);
            }
        });

        ITEM_STACK = SerializerFieldType.create((fieldName) -> new AbstractSerializerField<ItemStack>() {
            @Override
            public ItemStack readJsonRecipe(JsonObject jsonObject) {
                return SerializerHelper.getItemStack(jsonObject, fieldName);
            }

            @Override
            public ItemStack readBuffer(FriendlyByteBuf byteBuf) {
                return byteBuf.readItem();
            }

            @Override
            public void writeToBuffer(FriendlyByteBuf byteBuf) {
                byteBuf.writeItem(value);
            }
        });

        TIER = SerializerFieldType.create((fieldName) -> new AbstractSerializerField<GMTier>() {
            @Override
            public GMTier readJsonRecipe(JsonObject jsonObject) {
                return RecipeJsonHelper.deserializeEnum(jsonObject, fieldName, GMTier.values());
            }

            @Override
            public GMTier readBuffer(FriendlyByteBuf byteBuf) {
                return GMTier.values()[byteBuf.readVarInt()];
            }

            @Override
            public void writeToBuffer(FriendlyByteBuf byteBuf) {
                byteBuf.writeVarInt(value.ordinal());
            }
        });
    }
}
