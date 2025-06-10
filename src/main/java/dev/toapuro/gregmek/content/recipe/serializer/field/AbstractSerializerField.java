package dev.toapuro.gregmek.content.recipe.serializer.field;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public abstract class AbstractSerializerField<V> {
    V value;

    public abstract V readJsonRecipe(JsonObject jsonObject);

    public abstract V readBuffer(FriendlyByteBuf byteBuf);

    public abstract void writeToBuffer(FriendlyByteBuf byteBuf);

    public void loadFromJsonRecipe(JsonObject jsonObject) {
        value = readJsonRecipe(jsonObject);
    }

    public void loadFromBuffer(FriendlyByteBuf byteBuf) {
        value = readBuffer(byteBuf);
    }

    public V get() {
        return this.value;
    }

    public void set(V val) {
        this.value = val;
    }
}
