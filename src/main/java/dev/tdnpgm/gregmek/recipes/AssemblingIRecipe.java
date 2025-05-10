package dev.tdnpgm.gregmek.recipes;

import dev.tdnpgm.gregmek.registry.GregmekBlocks;
import dev.tdnpgm.gregmek.registry.GregmekRecipeSerializers;
import dev.tdnpgm.gregmek.registry.recipe.GregmekRecipeType;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AssemblingIRecipe extends AssemblingRecipe {
    public AssemblingIRecipe(ResourceLocation id, List<ItemStackIngredient> inputSolids, FluidStackIngredient inputFluid, FloatingLong energyRequired, int duration, ItemStack outputItem) {
        super(id, inputSolids, inputFluid, energyRequired, duration, outputItem);
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
