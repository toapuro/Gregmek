package dev.toapuro.gregmek.core.mixin;

import dev.toapuro.gregmek.common.helper.MixinHelper;
import dev.toapuro.gregmek.content.tile.component.TileComponentCircuit;
import dev.toapuro.gregmek.content.tile.interfaces.ITileProgrammable;
import dev.toapuro.gregmek.core.hooks.MixinHooks;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismProgressRecipeMixinHook;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = TileEntityProgressMachine.class, remap = false)
public abstract class TileEntityProgressMachineMixin<RECIPE extends MekanismRecipe> implements ITileProgrammable {
    @Shadow
    public int ticksRequired;

    @Unique
    private TileComponentCircuit gregmek$componentCircuit;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(IBlockProvider blockProvider, BlockPos pos, BlockState state, List<?> errorTypes, int baseTicksRequired, CallbackInfo ci) {
        gregmek$componentCircuit = new TileComponentCircuit(MixinHelper.cast(TileEntityMekanism.class, this));
    }

    @Inject(method = "recalculateUpgrades", at = @At("TAIL"))
    public void recalculateUpgrades(Upgrade upgrade, CallbackInfo ci) {
        TileEntityProgressMachine<?> tile = MixinHelper.cast(TileEntityProgressMachine.class, this);
        MixinHooks.invoke(IMekanismProgressRecipeMixinHook.class, hook ->
                hook.recalculateUpgrades(upgrade, tile));
        if (upgrade == Upgrade.SPEED) {
            for (IMekanismProgressRecipeMixinHook hook : MixinHooks.getHooks(IMekanismProgressRecipeMixinHook.class)) {
                ticksRequired = hook.modifyDurationTicks(ticksRequired, tile);
            }
        }
    }

    @Override
    public TileComponentCircuit gregmek$getCircuitComponent() {
        return gregmek$componentCircuit;
    }
}
