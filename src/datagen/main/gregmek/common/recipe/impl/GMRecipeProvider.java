package gregmek.common.recipe.impl;

import dev.toapuro.gregmek.Gregmek;
import gregmek.common.recipe.BaseRecipeProvider;
import gregmek.common.recipe.ISubRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

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
                new AlloyRecipeProvider(),
                new BendingRecipeProvider()
        );
    }

    private void addMiscRecipes(Consumer<FinishedRecipe> consumer) {
    }
}
