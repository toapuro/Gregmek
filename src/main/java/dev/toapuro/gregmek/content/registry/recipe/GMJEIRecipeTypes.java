package dev.toapuro.gregmek.content.registry.recipe;

import dev.toapuro.gregmek.content.recipe.AlloySmeltingRecipe;
import dev.toapuro.gregmek.content.recipe.AssemblingRecipe;
import dev.toapuro.gregmek.content.recipe.BendingRecipe;
import dev.toapuro.gregmek.content.registry.GMBlocks;
import mekanism.client.jei.MekanismJEIRecipeType;

public class GMJEIRecipeTypes {
    public static final MekanismJEIRecipeType<AssemblingRecipe> ASSEMBLING;
    public static final MekanismJEIRecipeType<AlloySmeltingRecipe> ALLOY_SMELTING;
    public static final MekanismJEIRecipeType<BendingRecipe> BENDING;

    static {
        ASSEMBLING = new MekanismJEIRecipeType<>(GMBlocks.ASSEMBLING_MACHINE, AssemblingRecipe.class);
        ALLOY_SMELTING = new MekanismJEIRecipeType<>(GMBlocks.ALLOY_SMELTER, AlloySmeltingRecipe.class);
        BENDING = new MekanismJEIRecipeType<>(GMBlocks.BENDER, BendingRecipe.class);
    }
}
