package dev.tdnpgm.gregmek.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GregmekIngredients {
    private enum AdditionalIngredientType {
        SINGLE,
        MULTI,
        ANY;

        AdditionalIngredientType() {
        }
    }

    @NothingNullByDefault
    public interface IAnyIngredient<STACK> extends InputIngredient<STACK> {
        @Override
        default boolean testType(@NotNull STACK stack) {
            return true;
        }

        @Override
        default long getNeededAmount(STACK stack) {
            return 0;
        }

        default boolean hasNoMatchingInstances() {
            return false;
        }

        default List<STACK> getRepresentations() {
            return new ArrayList<>();
        }

        default Ingredient getInputRaw() {
            return Ingredient.EMPTY;
        }

        default int getAmountRaw() {
            return 0;
        }

        default void write(FriendlyByteBuf buffer) {
            buffer.writeEnum(AdditionalIngredientType.ANY);
            Ingredient.EMPTY.toNetwork(buffer);
            buffer.writeVarInt(0);
        }

        default JsonElement serialize() {
            return new JsonObject();
        }

        @Override
        default boolean test(STACK stack) {
            return false;
        }
    }

    public static class AnyItemStackIngredient extends ItemStackIngredient implements IAnyIngredient<ItemStack> {
        @Override
        public @NotNull ItemStack getMatchingInstance(ItemStack itemStack) {
            return ItemStack.EMPTY;
        }
    }

    public static class AnyFluidStackIngredient extends FluidStackIngredient implements IAnyIngredient<FluidStack> {
        @Override
        public @NotNull FluidStack getMatchingInstance(FluidStack itemStack) {
            return FluidStack.EMPTY;
        }
    }

}
