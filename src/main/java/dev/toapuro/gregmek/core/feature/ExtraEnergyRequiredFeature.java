package dev.toapuro.gregmek.core.feature;

import com.google.gson.JsonObject;
import dev.toapuro.gregmek.core.hooks.MixinCompatibleSide;
import dev.toapuro.gregmek.core.hooks.MixinHooksHandler;
import dev.toapuro.gregmek.core.hooks.hook.IEnergyRequiredMixinHook;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismRecipeMixinHook;
import dev.toapuro.gregmek.core.interfaces.IHasExtraRecipeData;
import dev.toapuro.gregmek.core.interfaces.IHasExtraTileRecipeData;
import mekanism.api.SerializerHelper;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExtraEnergyRequiredFeature implements IMekanismRecipeMixinHook, IEnergyRequiredMixinHook {
    static {
        MixinHooksHandler.registerHook(new ExtraEnergyRequiredFeature());
    }

    private ExtraEnergyRequiredFeature() {
    }

    @Override
    public String getHookId() {
        return "extra_energy_required";
    }

    @Override
    public MixinCompatibleSide getSide() {
        return MixinCompatibleSide.COMMON;
    }

    @Override
    public <RECIPE extends Recipe<?>> void writeBuffer(FriendlyByteBuf buffer, RECIPE recipe) {
        if (recipe instanceof IHasExtraRecipeData energyRequiredHolder) {
            FloatingLong energyRequired = energyRequiredHolder.gregmek$getExtraEnergyRequired();
            energyRequired.writeToBuffer(buffer);
        }
    }

    @Override
    public <RECIPE extends Recipe<?>> void readBuffer(FriendlyByteBuf buffer, RECIPE recipe) {
        if (recipe instanceof IHasExtraRecipeData energyRequiredHolder) {
            energyRequiredHolder.gregmek$setExtraEnergyRequired(FloatingLong.readFromBuffer(buffer));
        }
    }

    @Override
    public <TILE extends TileEntityMekanism> FloatingLong modifyBaseEnergyPerTick(FloatingLong baseEnergy, TILE tile) {
        if (tile instanceof IHasExtraTileRecipeData extraEnergyRequired) {
            return baseEnergy.add(extraEnergyRequired.gregmek$getRecipeExtraEnergyRequired());
        }
        return baseEnergy;
    }

    @Override
    public <RECIPE extends Recipe<?>> void readFromJson(JsonObject json, RECIPE recipe) {
        if (recipe instanceof IHasExtraRecipeData energyRequiredHolder) {
            if (json.has("extraEnergyRequired")) {
                FloatingLong energyRequired = SerializerHelper.getFloatingLong(json, "extraEnergyRequired");
                energyRequiredHolder.gregmek$setExtraEnergyRequired(energyRequired);
            } else {
                energyRequiredHolder.gregmek$setExtraEnergyRequired(FloatingLong.ZERO); // FIX: maybe not needed
            }
        }
    }

    @Override
    public <RECIPE extends MekanismRecipe> void onCachedRecipeChanged(@Nullable CachedRecipe<RECIPE> cachedRecipe, TileEntityMekanism tile) {
        if (!(tile instanceof IHasExtraTileRecipeData tileHolder)) {
            return;
        }

        if (cachedRecipe == null) {
            tileHolder.gregmek$setRecipeExtraEnergyRequired(FloatingLong.ZERO);
            return;
        }

        RECIPE recipe = cachedRecipe.getRecipe();
        if (recipe instanceof IHasExtraRecipeData recipeHolder) {
            tileHolder.gregmek$setRecipeExtraEnergyRequired(recipeHolder.gregmek$getExtraEnergyRequired());
        }

        List<IEnergyContainer> energyContainers = tile.getEnergyContainers(null);
        for (IEnergyContainer energyContainer : energyContainers) {
            if (energyContainer instanceof MachineEnergyContainer<?> machineEnergyContainer) {
                machineEnergyContainer.updateEnergyPerTick();
            }
        }
    }
}
