package dev.toapuro.gregmek.core.mixin;

import dev.toapuro.gregmek.core.hooks.MixinHooksHandler;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismProgressRecipeMixinHook;
import gregmek.common.helper.MixinHelper;
import mekanism.api.Upgrade;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityProgressMachine.class)
public abstract class TileEntityProgressMachineMixin<RECIPE extends MekanismRecipe> {
    @Shadow
    public int ticksRequired;

    @Inject(method = "recalculateUpgrades", at = @At("TAIL"))
    public void recalculateUpgrades(Upgrade upgrade, CallbackInfo ci) {
        TileEntityProgressMachine<?> tile = MixinHelper.cast(this, TileEntityProgressMachine.class);
        MixinHooksHandler.execute(IMekanismProgressRecipeMixinHook.class, hook ->
                hook.recalculateUpgrades(upgrade, tile));
        if (upgrade == Upgrade.SPEED) {
            for (IMekanismProgressRecipeMixinHook hook : MixinHooksHandler.getHooks(IMekanismProgressRecipeMixinHook.class)) {
                ticksRequired = hook.modifyDurationTicks(ticksRequired, tile);
            }
        }
    }
}
