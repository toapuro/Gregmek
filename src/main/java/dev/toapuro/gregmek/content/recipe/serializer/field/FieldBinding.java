package dev.toapuro.gregmek.content.recipe.serializer.field;

import java.util.function.Function;

public record FieldBinding<RECIPE, V>(
        AbstractSerializerField<V> serializerField,
        Function<RECIPE, V> fieldExtractor
) {
    public void readFromRecipe(RECIPE recipe) {
        this.serializerField.set(fieldExtractor.apply(recipe));
    }
}
