package dev.toapuro.gregmek.core.hooks.hook;

import com.google.gson.JsonObject;
import dev.toapuro.gregmek.core.hooks.IMixinHook;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

public interface IMekanismRecipeMixinHook extends IMixinHook {
    default <RECIPE extends Recipe<?>> void writeBuffer(FriendlyByteBuf buffer, RECIPE recipe) {
    }

    default <RECIPE extends Recipe<?>> void readBuffer(FriendlyByteBuf buffer, RECIPE recipe) {
    }

    default <RECIPE extends Recipe<?>> void readFromJson(JsonObject json, RECIPE recipe) {
    }

    default <RECIPE extends MekanismRecipe> void onCachedRecipeChanged(@Nullable CachedRecipe<RECIPE> cachedRecipe, TileEntityMekanism tile) {
    }
}
