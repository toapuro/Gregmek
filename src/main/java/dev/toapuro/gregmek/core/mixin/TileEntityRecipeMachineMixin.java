package dev.toapuro.gregmek.core.mixin;

import dev.toapuro.gregmek.core.hooks.MixinHooksHandler;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismRecipeMixinHook;
import dev.toapuro.gregmek.core.interfaces.IHasExtraTileRecipeData;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.recipe.lookup.IRecipeLookupHandler;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TileEntityRecipeMachine.class)
@Implements(@Interface(iface = IRecipeLookupHandler.class, prefix = "irecipe$"))
public abstract class TileEntityRecipeMachineMixin<RECIPE extends MekanismRecipe> extends TileEntityConfigurableMachine implements IHasExtraTileRecipeData {
    @Unique
    private FloatingLong gregmek$recipeExtraEnergyRequired;

    public TileEntityRecipeMachineMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(IBlockProvider blockProvider, BlockPos pos, BlockState state, List<?> errorTypes, CallbackInfo ci) {
        this.gregmek$recipeExtraEnergyRequired = FloatingLong.ZERO;
    }

    @Override
    public FloatingLong gregmek$getRecipeExtraEnergyRequired() {
        return this.gregmek$recipeExtraEnergyRequired;
    }

    @Override
    public void gregmek$setRecipeExtraEnergyRequired(FloatingLong energyRequired) {
        this.gregmek$recipeExtraEnergyRequired = energyRequired;
    }

    public void irecipe$onCachedRecipeChanged(@Nullable CachedRecipe<RECIPE> cachedRecipe, int cacheIndex) {
        MixinHooksHandler.execute(IMekanismRecipeMixinHook.class, hook ->
                hook.onCachedRecipeChanged(cachedRecipe, this));
    }
}
