package dev.toapuro.gregmek.core.feature;

import dev.toapuro.gregmek.CommonConfig;
import dev.toapuro.gregmek.core.hooks.MixinCompatibleSide;
import dev.toapuro.gregmek.core.hooks.MixinHooksHandler;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismProgressRecipeMixinHook;
import mekanism.common.tile.prefab.TileEntityProgressMachine;

public class DurationMultiplierFeature implements IMekanismProgressRecipeMixinHook {
    static {
        MixinHooksHandler.registerHook(new DurationMultiplierFeature());
    }

    private DurationMultiplierFeature() {
    }

    @Override
    public String getHookId() {
        return "duration_multiplier";
    }

    @Override
    public MixinCompatibleSide getSide() {
        return MixinCompatibleSide.COMMON;
    }

    @Override
    public <TILE extends TileEntityProgressMachine<?>> int modifyDurationTicks(int duration, TILE tile) {
        return (int) (tile.ticksRequired * CommonConfig.CONFIG.processingDurationMultiplier.get());
    }
}
