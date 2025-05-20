package dev.tdnpgm.gregmek.recipes;

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
import java.util.function.BiPredicate;

public abstract class AlloySmelterRecipe extends MekanismRecipe implements BiPredicate<@NotNull ItemStack, @NotNull ItemStack> {
    private final ItemStackIngredient mainInput;
    private final ItemStackIngredient secondaryInput;
    private final ItemStack output;

    public AlloySmelterRecipe(ResourceLocation id, ItemStackIngredient mainInput, ItemStackIngredient secondaryInput, ItemStack output) {
        super(id);
        this.mainInput = Objects.requireNonNull(mainInput, "Main input cannot be null.");
        this.secondaryInput = Objects.requireNonNull(secondaryInput, "Secondary/Extra input cannot be null.");
        Objects.requireNonNull(output, "Output cannot be null.");
        if (output.isEmpty()) {
            throw new IllegalArgumentException("Output cannot be empty.");
        } else {
            this.output = output.copy();
        }
    }

    public boolean test(ItemStack input, ItemStack extra) {
        return this.mainInput.test(input) && this.secondaryInput.test(extra);
    }

    public ItemStackIngredient getMainInput() {
        return this.mainInput;
    }

    public ItemStackIngredient getSecondaryInput() {
        return this.secondaryInput;
    }

    @Contract(
            value = "_, _ -> new",
            pure = true
    )
    public ItemStack getOutput(@NotNull ItemStack input, @NotNull ItemStack extra) {
        return this.output.copy();
    }

    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return this.output.copy();
    }

    public List<ItemStack> getOutputDefinition() {
        return Collections.singletonList(this.output);
    }

    public boolean isIncomplete() {
        return this.mainInput.hasNoMatchingInstances() || this.secondaryInput.hasNoMatchingInstances();
    }

    public void write(FriendlyByteBuf buffer) {
        this.mainInput.write(buffer);
        this.secondaryInput.write(buffer);
        buffer.writeItem(this.output);
    }
}
