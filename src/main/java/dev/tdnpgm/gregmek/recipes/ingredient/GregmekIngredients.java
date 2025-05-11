package dev.tdnpgm.gregmek.recipes.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.InputIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class GregmekIngredients {

    @NothingNullByDefault
    public static abstract class AnyItemIngredient<STACK> implements InputIngredient<STACK> {
        private AnyItemIngredient() {
        }

        public boolean test(ItemStack stack) {
            return true;
        }

        public boolean testType(ItemStack stack) {
            return true;
        }

        public ItemStack getMatchingInstance(ItemStack stack) {
            return ItemStack.EMPTY;
        }

        public long getNeededAmount(ItemStack stack) {
            return 0;
        }

        public boolean hasNoMatchingInstances() {
            return false;
        }

        public List<STACK> getRepresentations() {
            return new ArrayList<>();
        }

        public Ingredient getInputRaw() {
            return Ingredient.EMPTY;
        }

        public int getAmountRaw() {
            return 0;
        }

        public void write(FriendlyByteBuf buffer) {
            buffer.writeEnum(AdditionalIngredientType.ANY);
            Ingredient.EMPTY.toNetwork(buffer);
            buffer.writeVarInt(0);
        }

        public JsonElement serialize() {
            return new JsonObject();
        }
    }

    private enum AdditionalIngredientType {
        SINGLE,
        MULTI,
        ANY;

        private AdditionalIngredientType() {
        }
    }

}
