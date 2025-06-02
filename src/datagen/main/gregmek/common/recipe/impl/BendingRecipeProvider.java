package gregmek.common.recipe.impl;

import dev.tdnpgm.gregmek.Gregmek;
import gregmek.common.recipe.ISubRecipeProvider;
import gregmek.common.recipe.builder.ItemFluidsRecipeBuilder;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registries.MekanismItems;
import mekanism.common.tags.MekanismTags;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class BendingRecipeProvider implements ISubRecipeProvider {
    @Override
    public void addRecipes(Consumer<FinishedRecipe> consumer) {
        String basePath = "bending/";

        ItemFluidsRecipeBuilder.bending(40, MekanismItems.STEEL_INGOT.getItemStack())
                .addItem(IngredientCreatorAccess.item().from(MekanismTags.Items.DUSTS_STEEL, 1))
                .build(consumer, Gregmek.rl(basePath + "dust_steel_ingot"));

        ItemFluidsRecipeBuilder.bending(40, MekanismItems.STEEL_INGOT.getItemStack())
                .addItem(IngredientCreatorAccess.item().from(MekanismTags.Items.NUGGETS_STEEL, 9))
                .build(consumer, Gregmek.rl(basePath + "steel_ingot"));
    }
}