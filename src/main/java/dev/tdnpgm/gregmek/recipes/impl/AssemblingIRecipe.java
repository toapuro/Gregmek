package dev.tdnpgm.gregmek.recipes.impl;

import dev.tdnpgm.gregmek.recipes.AssemblingRecipe;
import dev.tdnpgm.gregmek.registry.GregmekBlocks;
import dev.tdnpgm.gregmek.registry.GregmekRecipeSerializers;
import dev.tdnpgm.gregmek.registry.recipe.GregmekRecipeType;
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
        return GregmekRecipeType.ASSEMBLING.get();
    }

    public @NotNull RecipeSerializer<AssemblingRecipe> getSerializer() {
        return GregmekRecipeSerializers.ASSEMBLING.get();
    }

    public @NotNull String getGroup() {
        return GregmekBlocks.ASSEMBLING_MACHINE.getName();
    }

    public @NotNull ItemStack getToastSymbol() {
        return GregmekBlocks.ASSEMBLING_MACHINE.getItemStack();
    }
}
