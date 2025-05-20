package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.recipes.AlloySmelterRecipe;
import dev.tdnpgm.gregmek.recipes.AssemblingRecipe;
import mekanism.client.jei.MekanismJEIRecipeType;

public class GMJEIRecipeTypes {
    public static final MekanismJEIRecipeType<AssemblingRecipe> ASSEMBLING;
    public static final MekanismJEIRecipeType<AlloySmelterRecipe> ALLOY_SMELTER;

    static {
        ASSEMBLING = new MekanismJEIRecipeType<>(GMBlocks.ASSEMBLING_MACHINE, AssemblingRecipe.class);
        ALLOY_SMELTER = new MekanismJEIRecipeType<>(GMBlocks.ALLOY_SMELTER, AlloySmelterRecipe.class);
    }
}
