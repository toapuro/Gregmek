package dev.toapuro.gregmek.content.recipe.serializer.field;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public abstract class AbstractSerializerField<V> {
    V value;

    public abstract void readRecipe(JsonObject jsonObject);

    public abstract void readFromBuffer(FriendlyByteBuf byteBuf);

    public abstract void writeToBuffer(FriendlyByteBuf byteBuf);

    public V get() {
        return this.value;
    }

    public void set(V val) {
        this.value = val;
    }
}
