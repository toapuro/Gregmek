package dev.tdnpgm.gregmek.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.recipes.additional.IHasEnergyRequired;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = MachineEnergyContainer.class, remap = false)
public abstract class MachineEnergyContainerMixin<TILE extends TileEntityMekanism> {
    @Shadow
    @Final
    protected TILE tile;
    @Shadow
    @Final
    private FloatingLong baseEnergyPerTick;

    @ModifyReturnValue(method = "getBaseEnergyPerTick", at = @At("RETURN"))
    public FloatingLong modifyBaseEnergyPerTick(FloatingLong original) {
        if (tile instanceof IHasEnergyRequired energyRequiredHolder) {
            Gregmek.DEBUG_LOGGER.info("Modified Energy Container: {}", energyRequiredHolder.gregmek$getEnergyRequired());
            return baseEnergyPerTick.add(energyRequiredHolder.gregmek$getEnergyRequired());
        }
        return original;
    }
}
