package dev.toapuro.gregmek.mixin;

import dev.toapuro.gregmek.content.recipe.additional.IHasEnergyRequired;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.recipe.lookup.IRecipeLookupHandler;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.EnumUtils;
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

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(TileEntityRecipeMachine.class)
@Implements(@Interface(iface = IRecipeLookupHandler.class, prefix = "irecipe$"))
public abstract class TileEntityRecipeMachineMixin<RECIPE extends MekanismRecipe> extends TileEntityConfigurableMachine implements IHasEnergyRequired {
    @Unique
    private FloatingLong gregmek$recipeEnergyRequired;

    public TileEntityRecipeMachineMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(IBlockProvider blockProvider, BlockPos pos, BlockState state, List<?> errorTypes, CallbackInfo ci) {
        this.gregmek$recipeEnergyRequired = FloatingLong.ZERO;
    }

    @Override
    public FloatingLong gregmek$getEnergyRequired() {
        return this.gregmek$recipeEnergyRequired;
    }

    @Override
    public void gregmek$setEnergyRequired(FloatingLong energyRequired) {
        this.gregmek$recipeEnergyRequired = energyRequired;
    }

    public void irecipe$onCachedRecipeChanged(@Nullable CachedRecipe<RECIPE> cachedRecipe, int cacheIndex) {
        if (cachedRecipe == null) {
            gregmek$recipeEnergyRequired = FloatingLong.ZERO;
            return;
        }

        RECIPE recipe = cachedRecipe.getRecipe();
        if (recipe instanceof IHasEnergyRequired recipeRequiredEnergyHolder) {
            gregmek$recipeEnergyRequired = recipeRequiredEnergyHolder.gregmek$getEnergyRequired();
        }

        Set<IEnergyContainer> energyContainerSet = Arrays.stream(EnumUtils.DIRECTIONS)
                .map(this::getEnergyContainers)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        for (IEnergyContainer energyContainer : energyContainerSet) {
            if (energyContainer instanceof MachineEnergyContainer<?> machineEnergyContainer) {
                machineEnergyContainer.updateEnergyPerTick();
            }
        }
    }
}
