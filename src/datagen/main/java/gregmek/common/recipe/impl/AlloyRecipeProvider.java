package java.gregmek.common.recipe.impl;

import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import mekanism.common.registries.MekanismItems;
import mekanism.common.tags.MekanismTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;

import java.gregmek.common.recipe.ISubRecipeProvider;
import java.gregmek.common.recipe.builder.AssemblingRecipeBuilder;
import java.util.function.Consumer;

public class AlloyRecipeProvider implements ISubRecipeProvider {
    @Override
    public void addRecipes(Consumer<FinishedRecipe> consumer) {
        String basePath = "alloy_smelting/";

        AssemblingRecipeBuilder.assembling(40, MekanismItems.STEEL_DUST.getItemStack())
                .addItem(IngredientCreatorAccess.item().from(Items.IRON_INGOT, 1))
                .addItem(IngredientCreatorAccess.item().from(MekanismTags.Items.DUSTS_STEEL, 1))
                .build(consumer, Mekanism.rl(basePath + "steel_dust"));
    }
}