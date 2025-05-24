package dev.tdnpgm.gregmek.recipes.impl;

import dev.tdnpgm.gregmek.recipes.BendingRecipe;
import dev.tdnpgm.gregmek.registry.GMBlocks;
import dev.tdnpgm.gregmek.registry.GMRecipeSerializers;
import dev.tdnpgm.gregmek.registry.recipe.GMRecipeType;
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
