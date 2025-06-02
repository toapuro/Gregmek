package gregmek.common.recipe.impl;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.common.helper.FloatingLongHelper;
import gregmek.common.recipe.ISubRecipeProvider;
import gregmek.common.recipe.builder.ItemFluidsRecipeBuilder;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registries.MekanismItems;
import mekanism.common.tags.MekanismTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class AssemblingRecipeProvider implements ISubRecipeProvider {
    @Override
    public void addRecipes(Consumer<FinishedRecipe> consumer) {
        String basePath = "assembling/";

        ItemFluidsRecipeBuilder.assembling(40, MekanismItems.ENERGY_TABLET.getItemStack())
                .addItem(IngredientCreatorAccess.item().from(MekanismTags.Items.ENRICHED_REDSTONE, 1))
                .addItem(IngredientCreatorAccess.item().from(MekanismTags.Items.ALLOYS_INFUSED, 2))
                .addItem(IngredientCreatorAccess.item().from(MekanismTags.Items.ENRICHED_GOLD, 1))
                .extraEnergyRequired(FloatingLongHelper.getFloatingLong(1000, FloatingLongHelper.Unit.KILO))
                .build(consumer, Gregmek.rl(basePath + "energy_tablet"));

        ItemFluidsRecipeBuilder.assembling(40, new ItemStack(Items.BOW, 1))
                .addItem(IngredientCreatorAccess.item().from(Items.STRING, 1))
                .addItem(IngredientCreatorAccess.item().from(Items.BOW, 1))
                .build(consumer, Gregmek.rl(basePath + "bow"));
    }
}
