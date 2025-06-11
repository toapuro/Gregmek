package dev.toapuro.gregmek.core.hooks.hook;

import dev.toapuro.gregmek.core.hooks.IMixinHook;
import mekanism.api.math.FloatingLong;
import mekanism.common.tile.base.TileEntityMekanism;

public interface IEnergyRequiredMixinHook extends IMixinHook {
    default <TILE extends TileEntityMekanism> FloatingLong modifyBaseEnergyPerTick(FloatingLong baseEnergy, TILE tile) {
        return baseEnergy;
    }

}
