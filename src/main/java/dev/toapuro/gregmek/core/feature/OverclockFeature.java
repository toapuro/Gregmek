package dev.toapuro.gregmek.core.feature;

import dev.toapuro.gregmek.content.item.ItemProgrammedCircuit;
import dev.toapuro.gregmek.content.tile.interfaces.ITileProgrammable;
import dev.toapuro.gregmek.content.tile.inventory.CircuitVirtualInventorySlot;
import dev.toapuro.gregmek.core.hooks.MixinCompatibleSide;
import dev.toapuro.gregmek.core.hooks.MixinHooks;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismProgressRecipeMixinHook;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismRecipeMixinHook;
import dev.toapuro.gregmek.core.interfaces.IHasExtraTileRecipeData;
import mekanism.common.tile.prefab.TileEntityProgressMachine;

public class OverclockFeature implements IMekanismProgressRecipeMixinHook, IMekanismRecipeMixinHook {
    static {
        MixinHooks.registerHook(new OverclockFeature());
    }

    private OverclockFeature() {
    }

    @Override
    public String getHookId() {
        return "overclock";
    }

    @Override
    public MixinCompatibleSide getSide() {
        return MixinCompatibleSide.COMMON;
    }

    @Override
    public <TILE extends TileEntityProgressMachine<?>> int modifyDurationTicks(int duration, TILE tile) {
        if (tile instanceof ITileProgrammable programmable && tile instanceof IHasExtraTileRecipeData recipeData) {
            CircuitVirtualInventorySlot circuitSlot = programmable.gregmek$getCircuitComponent().getCircuitSlot();
            ItemProgrammedCircuit circuit = (ItemProgrammedCircuit) circuitSlot.getStack().getItem();
            int ordinalDelta = circuit.getCircuitTier().ordinal() - recipeData.gregmek$getRecipeTier().ordinal();
            int ocDivisor = 1 << Math.max(0, ordinalDelta); // 2^delta
            return duration / ocDivisor;
        }
        return duration;
    }
}
