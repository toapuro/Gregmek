package dev.toapuro.gregmek.content.recipe.impl;

import dev.toapuro.gregmek.content.recipe.AlloySmeltingRecipe;
import dev.toapuro.gregmek.content.registry.GMBlocks;
import dev.toapuro.gregmek.content.registry.GMRecipeSerializers;
import dev.toapuro.gregmek.content.registry.recipe.GMRecipeType;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public class AlloySmeltingIRecipe extends AlloySmeltingRecipe {
    public AlloySmeltingIRecipe(ResourceLocation id, ItemStackIngredient mainInput, ItemStackIngredient secondaryInput, int duration, ItemStack output) {
        super(id, mainInput, secondaryInput, duration, output);
    }

    public @NotNull RecipeType<AlloySmeltingRecipe> getType() {
        return GMRecipeType.ALLOY_SMELTING.get();
    }

    public @NotNull RecipeSerializer<AlloySmeltingRecipe> getSerializer() {
        return GMRecipeSerializers.ALLOY_SMELTING.get();
    }

    public @NotNull String getGroup() {
        return GMBlocks.ALLOY_SMELTER.getName();
    }

    public @NotNull ItemStack getToastSymbol() {
        return GMBlocks.ALLOY_SMELTER.getItemStack();
    }
}
