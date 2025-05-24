package dev.tdnpgm.gregmek.recipes.impl;

import dev.tdnpgm.gregmek.recipes.AlloySmeltingRecipe;
import dev.tdnpgm.gregmek.registry.GMBlocks;
import dev.tdnpgm.gregmek.registry.GMRecipeSerializers;
import dev.tdnpgm.gregmek.registry.recipe.GMRecipeType;
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
