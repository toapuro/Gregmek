package dev.toapuro.gregmek.content.recipe.serializer.builder;

import com.google.gson.JsonObject;
import dev.toapuro.gregmek.content.recipe.serializer.field.FieldBinding;
import mekanism.api.recipes.MekanismRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class RecipeSerializerFactory {
    public static <R extends MekanismRecipe> RecipeSerializer<R> composeSerializer(Supplier<SerializerBuilder.BuilderResult<R>> constructor) {
        SerializerBuilder.BuilderResult<R> result = constructor.get();
        return new RecipeSerializer<>() {
            @Override
            public @NotNull R fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
                for (FieldBinding<R, ?> fieldBinding : result.fieldBindings()) {
                    fieldBinding.serializerField().loadFromJsonRecipe(jsonObject);
                }
                return result.recipeDeserializer().apply(resourceLocation);
            }

            @Override
            public @Nullable R fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf byteBuf) {
                for (FieldBinding<R, ?> fieldBinding : result.fieldBindings()) {
                    fieldBinding.serializerField().loadFromBuffer(byteBuf);
                }
                return result.recipeDeserializer().apply(resourceLocation);
            }

            @Override
            public void toNetwork(@NotNull FriendlyByteBuf byteBuf, @NotNull R recipe) {
                // レシピからバッファーへ出力
                for (FieldBinding<R, ?> fieldBinding : result.fieldBindings()) {
                    fieldBinding.loadFromRecipe(recipe);
                    fieldBinding.serializerField().writeToBuffer(byteBuf);
                }
            }
        };
    }
}
