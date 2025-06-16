package dev.toapuro.gregmek.core.mixin;

import dev.toapuro.gregmek.common.helper.MixinHelper;
import dev.toapuro.gregmek.common.utils.GregmekUtils;
import dev.toapuro.gregmek.core.hooks.MixinHooks;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismContainerMixinHook;
import dev.toapuro.gregmek.core.interfaces.IHasCircuitSlot;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.VirtualInventoryContainerSlot;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MekanismTileContainer.class, remap = false)
public abstract class MekanismTileContainerMixin<TILE extends TileEntityMekanism> extends MekanismContainer implements IHasCircuitSlot {
    @Shadow
    @Final
    @NotNull
    protected TILE tile;

    @Unique
    private VirtualInventoryContainerSlot gregmek$circuitSlot;

    protected MekanismTileContainerMixin(ContainerTypeRegistryObject<?> type, int id, Inventory inv) {
        super(type, id, inv);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "addSlots", at = @At("HEAD"))
    protected void addSlots(CallbackInfo ci) {
        super.addSlots();
        MixinHooks.invoke(IMekanismContainerMixinHook.class, hook ->
                GregmekUtils.safeCast(MekanismTileContainer.class, this)
                        .ifPresent(container -> hook.addSlots(container, tile, this::addSlot)));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotID) {
        Slot slot = getSlot(slotID);
        ItemStack stackToInsert = slot.getItem();

        for (IMekanismContainerMixinHook hook : MixinHooks.getHooks(IMekanismContainerMixinHook.class)) {
            stackToInsert = hook.quickMoveStackBefore(stackToInsert, MixinHelper.cast(this), tile, selectedWindow);
        }

        super.quickMoveStack(player, slotID);

        for (IMekanismContainerMixinHook hook : MixinHooks.getHooks(IMekanismContainerMixinHook.class)) {
            stackToInsert = hook.quickMoveStackAfter(stackToInsert, MixinHelper.cast(this), tile, selectedWindow);
        }
        return stackToInsert;
    }

    @Override
    public VirtualInventoryContainerSlot gregmek$getCircuitSlot() {
        return this.gregmek$circuitSlot;
    }

    @Override
    public void gregmek$setCircuitSlot(VirtualInventoryContainerSlot circuitSlot) {
        this.gregmek$circuitSlot = circuitSlot;
    }
}
