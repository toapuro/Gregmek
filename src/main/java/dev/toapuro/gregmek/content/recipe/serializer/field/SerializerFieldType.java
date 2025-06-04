package dev.toapuro.gregmek.content.recipe.serializer.field;

import java.util.function.Function;

public class SerializerFieldType<V> {
    private final Function<String, AbstractSerializerField<V>> generator;

    public SerializerFieldType(Function<String, AbstractSerializerField<V>> generator) {
        this.generator = generator;
    }

    public static <V> SerializerFieldType<V> create(Function<String, AbstractSerializerField<V>> supplier) {
        return new SerializerFieldType<>(supplier);
    }

    public AbstractSerializerField<V> define(String fieldName) {
        return this.generator.apply(fieldName);
    }
}
