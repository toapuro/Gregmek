package dev.toapuro.gregmek.content.recipe;

import dev.toapuro.gregmek.common.api.exceptions.GMRecipeError;
import dev.toapuro.gregmek.common.helper.RecipeHelper;
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

public abstract class AlloySmeltingRecipe extends MekanismRecipe implements Predicate<List<ItemStack>> {
    public static int MAX_ITEM_SLOTS = 2;
    private final ItemStackIngredient mainInput;
    private final ItemStackIngredient secondaryInput;
    private final int duration;
    private final ItemStack output;

    public AlloySmeltingRecipe(ResourceLocation id, ItemStackIngredient mainInput, ItemStackIngredient secondaryInput, int duration, ItemStack output) {
        super(id);

        this.mainInput = Objects.requireNonNull(mainInput, GMRecipeError.INPUT_NULL.getMessage());
        this.secondaryInput = Objects.requireNonNull(secondaryInput, GMRecipeError.INPUT_NULL.getMessage());
        this.duration = duration;
        this.output = RecipeHelper.requireNotEmpty(output, GMRecipeError.OUTPUT_NULL, GMRecipeError.OUTPUT_EMPTY).copy();
    }

    public boolean test(List<ItemStack> inputs) {
        return inputs.stream().anyMatch(mainInput) && inputs.stream().anyMatch(secondaryInput);
    }

    public List<ItemStackIngredient> getInputSolids() {
        return List.of(mainInput, secondaryInput);
    }

    public ItemStackIngredient getMainInput() {
        return mainInput;
    }

    public ItemStackIngredient getSecondaryInput() {
        return secondaryInput;
    }

    public ItemStack getOutput() {
        return output;
    }

    public int getDuration() {
        return this.duration;
    }

    @Contract(
            value = "_ -> new",
            pure = true
    )
    public ItemStack getOutput(List<ItemStack> itemStacks) {
        return this.output.copy();
    }

    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return this.output.copy();
    }

    public List<ItemStack> getOutputDefinition() {
        return Collections.singletonList(this.output);
    }

    public boolean isIncomplete() {
        return false;
//        return this.mainInput.hasNoMatchingInstances() || this.secondaryInput.hasNoMatchingInstances();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
    }
}
