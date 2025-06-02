package dev.toapuro.gregmek.content.recipe.impl;

import dev.toapuro.gregmek.content.recipe.BendingRecipe;
import dev.toapuro.gregmek.content.registry.GMBlocks;
import dev.toapuro.gregmek.content.registry.GMRecipeSerializers;
import dev.toapuro.gregmek.content.registry.recipe.GMRecipeType;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BendingIRecipe extends BendingRecipe {
    public BendingIRecipe(ResourceLocation id, List<ItemStackIngredient> inputSolids, int duration, ItemStack output) {
        super(id, inputSolids, duration, output);
    }

    public @NotNull RecipeType<BendingRecipe> getType() {
        return GMRecipeType.BENDING.get();
    }

    public @NotNull RecipeSerializer<BendingRecipe> getSerializer() {
        return GMRecipeSerializers.BENDING.get();
    }

    public @NotNull String getGroup() {
        return GMBlocks.ALLOY_SMELTER.getName();
    }

    public @NotNull ItemStack getToastSymbol() {
        return GMBlocks.ALLOY_SMELTER.getItemStack();
    }
}
