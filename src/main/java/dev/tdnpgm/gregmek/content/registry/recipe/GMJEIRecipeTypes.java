package dev.tdnpgm.gregmek.content.registry.recipe;

import dev.tdnpgm.gregmek.content.recipe.AlloySmeltingRecipe;
import dev.tdnpgm.gregmek.content.recipe.AssemblingRecipe;
import dev.tdnpgm.gregmek.content.recipe.BendingRecipe;
import dev.tdnpgm.gregmek.content.registry.GMBlocks;
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
