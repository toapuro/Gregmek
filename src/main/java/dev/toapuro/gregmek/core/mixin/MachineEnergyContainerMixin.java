package dev.toapuro.gregmek.core.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.toapuro.gregmek.core.hooks.MixinHooksHandler;
import dev.toapuro.gregmek.core.hooks.hook.IEnergyRequiredMixinHook;
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

    @ModifyReturnValue(method = "getBaseEnergyPerTick", at = @At("RETURN"))
    public FloatingLong modifyBaseEnergyPerTick(FloatingLong original) {
        FloatingLong modified = original.copy();
        for (IEnergyRequiredMixinHook hook : MixinHooksHandler.getHooks(IEnergyRequiredMixinHook.class)) {
            modified = hook.modifyBaseEnergyPerTick(modified, tile);
        }
        return modified;
    }
}
