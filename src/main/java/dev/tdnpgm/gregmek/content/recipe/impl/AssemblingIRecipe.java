package dev.tdnpgm.gregmek.content.recipe.impl;

import dev.tdnpgm.gregmek.content.recipe.AssemblingRecipe;
import dev.tdnpgm.gregmek.content.registry.GMBlocks;
import dev.tdnpgm.gregmek.content.registry.GMRecipeSerializers;
import dev.tdnpgm.gregmek.content.registry.recipe.GMRecipeType;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AssemblingIRecipe extends AssemblingRecipe {
    public AssemblingIRecipe(ResourceLocation id, List<ItemStackIngredient> inputSolids, List<FluidStackIngredient> inputFluids, int duration, ItemStack outputItem) {
        super(id, inputSolids, inputFluids, duration, outputItem);
    }

    public @NotNull RecipeType<AssemblingRecipe> getType() {
        return GMRecipeType.ASSEMBLING.get();
    }

    public @NotNull RecipeSerializer<AssemblingRecipe> getSerializer() {
        return GMRecipeSerializers.ASSEMBLING.get();
    }

    public @NotNull String getGroup() {
        return GMBlocks.ASSEMBLING_MACHINE.getName();
    }

    public @NotNull ItemStack getToastSymbol() {
        return GMBlocks.ASSEMBLING_MACHINE.getItemStack();
    }
}
