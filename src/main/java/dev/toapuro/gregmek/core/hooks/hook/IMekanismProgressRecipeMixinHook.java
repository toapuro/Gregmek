package dev.toapuro.gregmek.core.hooks.hook;

import dev.toapuro.gregmek.core.hooks.IMixinHook;
import mekanism.api.Upgrade;
import mekanism.common.tile.prefab.TileEntityProgressMachine;

public interface IMekanismProgressRecipeMixinHook extends IMixinHook {
    default <TILE extends TileEntityProgressMachine<?>> void recalculateUpgrades(Upgrade upgrade, TILE tile) {
    }

    default <TILE extends TileEntityProgressMachine<?>> int modifyDurationTicks(int duration, TILE tile) {
        return duration;
    }
}
