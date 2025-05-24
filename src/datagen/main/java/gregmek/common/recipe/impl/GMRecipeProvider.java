package java.gregmek.common.recipe.impl;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.registry.GMBlocks;
import mekanism.common.Mekanism;
import mekanism.common.registries.MekanismItems;
import mekanism.common.tags.MekanismTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.gregmek.common.recipe.BaseRecipeProvider;
import java.gregmek.common.recipe.ISubRecipeProvider;
import java.gregmek.common.recipe.builder.MekDataShapedRecipeBuilder;
import java.gregmek.common.recipe.pattern.Pattern;
import java.gregmek.common.recipe.pattern.RecipePattern;
import java.gregmek.common.recipe.pattern.RecipePattern.TripleLine;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class GMRecipeProvider extends BaseRecipeProvider {
    private final List<ISubRecipeProvider> compatProviders = new ArrayList<>();
    private final Set<String> disabledCompats = new HashSet<>();

    public GMRecipeProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, existingFileHelper, Gregmek.MODID);
    }

    public Set<String> getDisabledCompats() {
        return Collections.unmodifiableSet(disabledCompats);
    }

    @Override
    protected void addRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        addMiscRecipes(consumer);
        compatProviders.forEach(compatProvider -> compatProvider.addRecipes(consumer));
    }

    @Override
    protected @NotNull List<ISubRecipeProvider> getSubRecipeProviders() {
        return List.of(
                new AssemblingRecipeProvider(),
                new AlloyRecipeProvider()
        );
    }

    private void addMiscRecipes(Consumer<FinishedRecipe> consumer) {
        //Atomic disassembler
        MekDataShapedRecipeBuilder.shapedRecipe(GMBlocks.ASSEMBLING_MACHINE)
                .pattern(RecipePattern.createPattern(
                        TripleLine.of(Pattern.ALLOY, Pattern.CONSTANT, Pattern.ALLOY),
                        TripleLine.of(Pattern.ALLOY, Pattern.ENERGY, Pattern.ALLOY),
                        TripleLine.of(Pattern.INGOT, Pattern.INGOT, Pattern.INGOT))
                ).key(Pattern.ENERGY, MekanismItems.ENERGY_TABLET)
                .key(Pattern.INGOT, MekanismTags.Items.INGOTS_STEEL)
                .key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_INFUSED)
                .key(Pattern.CONSTANT, MekanismTags.Items.ALLOYS_INFUSED)
                .category(RecipeCategory.MISC)
                .build(consumer);
    }
}
