package dev.toapuro.gregmek.content.recipe;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class BendingRecipe extends MekanismRecipe implements Predicate<List<ItemStack>> {
    public static int MAX_ITEM_SLOTS = 2;
    public static int MAX_FLUID_SLOTS = 1;
    private final List<ItemStackIngredient> inputSolids;
    private final int duration;
    private final ItemStack outputItem;

    public BendingRecipe(ResourceLocation id, List<ItemStackIngredient> inputSolids, int duration, ItemStack outputItem) {
        super(id);
        this.inputSolids = Objects.requireNonNull(inputSolids, "Main input cannot be null.");
        this.duration = duration;
        Objects.requireNonNull(outputItem, "Output cannot be null.");
        if (outputItem.isEmpty()) {
            throw new IllegalArgumentException("Output cannot be empty.");
        } else {
            this.outputItem = outputItem.copy();
        }
    }

    public boolean test(List<ItemStack> inputs) {
        for (ItemStackIngredient inputSolid : inputSolids) {
            if (inputs.stream().noneMatch(inputSolid)) {
                return false;
            }
        }
        return true;
    }

    public List<ItemStackIngredient> getInputSolids() {
        return this.inputSolids;
    }

    public int getDuration() {
        return this.duration;
    }

    @Contract(
            value = "_ -> new",
            pure = true
    )
    public ItemStack getOutput(List<ItemStack> itemStacks) {
        return this.outputItem.copy();
    }

    @Contract(
            value = " -> new",
            pure = true
    )
    public ItemStack getOutput() {
        return this.outputItem.copy();
    }

    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return this.outputItem.copy();
    }

    public List<ItemStack> getOutputDefinition() {
        return Collections.singletonList(this.outputItem);
    }

    public boolean isIncomplete() {
        return false;
//        return this.mainInput.hasNoMatchingInstances() || this.secondaryInput.hasNoMatchingInstances();
    }

    public void write(FriendlyByteBuf buffer) {
    }
}
