package dev.tdnpgm.gregmek.recipes.impl;

import dev.tdnpgm.gregmek.recipes.AlloySmelterRecipe;
import dev.tdnpgm.gregmek.registry.GMBlocks;
import dev.tdnpgm.gregmek.registry.GMRecipeSerializers;
import dev.tdnpgm.gregmek.registry.recipe.GMRecipeType;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public class AlloySmelterIRecipe extends AlloySmelterRecipe {
    public AlloySmelterIRecipe(ResourceLocation id, ItemStackIngredient mainInput, ItemStackIngredient secondaryInput, ItemStack output) {
        super(id, mainInput, secondaryInput, output);
    }

    public @NotNull RecipeType<AlloySmelterRecipe> getType() {
        return GMRecipeType.ALLOY_SMELTER.get();
    }

    public @NotNull RecipeSerializer<AlloySmelterRecipe> getSerializer() {
        return GMRecipeSerializers.ALLOY_SMELTER.get();
    }

    public @NotNull String getGroup() {
        return GMBlocks.ALLOY_SMELTER.getName();
    }

    public @NotNull ItemStack getToastSymbol() {
        return GMBlocks.ALLOY_SMELTER.getItemStack();
    }
}
