package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.recipes.AssemblingRecipe;
import mekanism.client.jei.MekanismJEIRecipeType;

public class GregmekJEIRecipeTypes {
    public static final MekanismJEIRecipeType<AssemblingRecipe> ASSEMBLING;

    static {
        ASSEMBLING = new MekanismJEIRecipeType<>(GregmekBlocks.ASSEMBLING_MACHINE, AssemblingRecipe.class);
    }
}
