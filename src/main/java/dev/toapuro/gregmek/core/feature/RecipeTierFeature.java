package dev.toapuro.gregmek.core.feature;

import com.google.gson.JsonObject;
import dev.toapuro.gregmek.common.helper.RecipeJsonHelper;
import dev.toapuro.gregmek.content.tier.GMTier;
import dev.toapuro.gregmek.core.hooks.MixinCompatibleSide;
import dev.toapuro.gregmek.core.hooks.MixinHooks;
import dev.toapuro.gregmek.core.hooks.hook.IEnergyRequiredMixinHook;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismRecipeMixinHook;
import dev.toapuro.gregmek.core.interfaces.IHasExtraRecipeData;
import dev.toapuro.gregmek.core.interfaces.IHasExtraTileRecipeData;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

public class RecipeTierFeature implements IMekanismRecipeMixinHook, IEnergyRequiredMixinHook {
    static {
        MixinHooks.registerHook(new RecipeTierFeature());
    }

    private RecipeTierFeature() {
    }

    @Override
    public String getHookId() {
        return "recipe_tier";
    }

    @Override
    public MixinCompatibleSide getSide() {
        return MixinCompatibleSide.COMMON;
    }

    @Override
    public <RECIPE extends Recipe<?>> void writeBuffer(FriendlyByteBuf buffer, RECIPE recipe) {
        if (recipe instanceof IHasExtraRecipeData extraRecipeData) {
            buffer.writeUtf(extraRecipeData.gregmek$getRecipeTier().name());
        }
    }

    @Override
    public <RECIPE extends Recipe<?>> void readBuffer(FriendlyByteBuf buffer, RECIPE recipe) {
        if (recipe instanceof IHasExtraRecipeData extraRecipeData) {
            GMTier gmTier = GMTier.valueOf(buffer.readUtf());
            extraRecipeData.gregmek$setRecipeTier(gmTier);
        }
    }

    @Override
    public <RECIPE extends Recipe<?>> void readFromJson(JsonObject json, RECIPE recipe) {
        if (recipe instanceof IHasExtraRecipeData energyRequiredHolder) {
            if (json.has("recipeTier")) {
                GMTier recipeTier = RecipeJsonHelper.deserializeEnum(json, "recipeTier", GMTier.values());
                energyRequiredHolder.gregmek$setRecipeTier(recipeTier);
            } else {
                energyRequiredHolder.gregmek$setRecipeTier(GMTier.PRIMITIVE);
            }
        }
    }

    @Override
    public <RECIPE extends MekanismRecipe> void onCachedRecipeChanged(@Nullable CachedRecipe<RECIPE> cachedRecipe, TileEntityMekanism tile) {
        if (!(tile instanceof IHasExtraTileRecipeData tileHolder)) {
            return;
        }

        if (cachedRecipe == null) {
            tileHolder.gregmek$setRecipeTier(GMTier.PRIMITIVE);
            return;
        }

        RECIPE recipe = cachedRecipe.getRecipe();
        if (recipe instanceof IHasExtraRecipeData recipeHolder) {
            tileHolder.gregmek$setRecipeTier(recipeHolder.gregmek$getRecipeTier());
        }
    }
}
