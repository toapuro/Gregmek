package gregmek.common.recipe.impl;

import dev.toapuro.gregmek.Gregmek;
import gregmek.common.recipe.ISubRecipeProvider;
import gregmek.common.recipe.builder.TwoItemStackToItemStackRecipeBuilder;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registries.MekanismItems;
import mekanism.common.tags.MekanismTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class AlloyRecipeProvider implements ISubRecipeProvider {
    @Override
    public void addRecipes(Consumer<FinishedRecipe> consumer) {
        String basePath = "alloy_smelting/";

        TwoItemStackToItemStackRecipeBuilder.alloySmelting(
                        200,
                        IngredientCreatorAccess.item().from(Items.IRON_INGOT, 1),
                        IngredientCreatorAccess.item().from(MekanismTags.Items.DUSTS_COAL, 1),
                        MekanismItems.STEEL_DUST.getItemStack())
                .build(consumer, Gregmek.rl(basePath + "steel_dust"));
    }
}