package dev.toapuro.gregmek.content.recipe.serializer.builder;

import dev.toapuro.gregmek.content.recipe.serializer.field.AbstractSerializerField;
import dev.toapuro.gregmek.content.recipe.serializer.field.FieldBinding;
import mekanism.api.recipes.MekanismRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SerializerBuilder<RECIPE extends MekanismRecipe> {
    private final List<FieldBinding<RECIPE, ?>> fieldBindings;

    private SerializerBuilder() {
        this.fieldBindings = new ArrayList<>();
    }

    public static <R extends MekanismRecipe> SerializerBuilder<R> create(Class<R> rClass) {
        return new SerializerBuilder<>();
    }

    public <V> SerializerBuilder<RECIPE> bind(AbstractSerializerField<V> serializerField, Function<RECIPE, V> fieldExtractor) {
        fieldBindings.add(new FieldBinding<>(serializerField, fieldExtractor));
        return this;
    }

    public SerializerBuilder.BuilderResult<RECIPE> build(Function<ResourceLocation, RECIPE> recipeDeserializer) {
        return new SerializerBuilder.BuilderResult<>(fieldBindings, recipeDeserializer);
    }

    public record BuilderResult<RECIPE extends MekanismRecipe>(
            List<FieldBinding<RECIPE, ?>> fieldBindings,
            Function<ResourceLocation, RECIPE> recipeDeserializer
    ) {
    }
}
