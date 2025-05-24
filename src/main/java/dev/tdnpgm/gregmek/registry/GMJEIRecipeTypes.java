package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.recipes.AlloySmeltingRecipe;
import dev.tdnpgm.gregmek.recipes.AssemblingRecipe;
import dev.tdnpgm.gregmek.recipes.BendingRecipe;
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
