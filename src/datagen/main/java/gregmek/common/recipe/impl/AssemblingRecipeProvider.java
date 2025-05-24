package java.gregmek.common.recipe.impl;

import mekanism.api.datagen.recipe.builder.PressurizedReactionRecipeBuilder;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import mekanism.common.registries.MekanismGases;
import mekanism.common.registries.MekanismItems;
import mekanism.common.tags.MekanismTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.gregmek.common.recipe.BaseRecipeProvider;
import java.gregmek.common.recipe.ISubRecipeProvider;
import java.gregmek.common.recipe.builder.AssemblingRecipeBuilder;
import java.util.List;
import java.util.function.Consumer;

public class AssemblingRecipeProvider implements ISubRecipeProvider {
    @Override
    public void addRecipes(Consumer<FinishedRecipe> consumer) {
        String basePath = "assembling/";

        AssemblingRecipeBuilder.assembling(40, MekanismItems.ENERGY_TABLET.getItemStack())
                .addItem(IngredientCreatorAccess.item().from(MekanismTags.Items.ENRICHED_REDSTONE, 1))
                .addItem(IngredientCreatorAccess.item().from(MekanismTags.Items.ALLOYS_INFUSED, 2))
                .addItem(IngredientCreatorAccess.item().from(MekanismTags.Items.ENRICHED_GOLD, 1))
                .build(consumer, Mekanism.rl(basePath + "energy_tablet"));

        AssemblingRecipeBuilder.assembling(40, new ItemStack(Items.BOW, 2))
                .addItem(IngredientCreatorAccess.item().from(Items.STRING, 2))
                .addItem(IngredientCreatorAccess.item().from(Items.BOW, 3))
                .build(consumer, Mekanism.rl(basePath + "bow"));
    }
}
