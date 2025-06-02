package dev.toapuro.gregmek.content.tile.container;

import dev.toapuro.gregmek.content.tile.interfaces.ITileProgrammable;
import mekanism.common.inventory.container.slot.VirtualInventoryContainerSlot;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GMTileContainer<TILE extends TileEntityMekanism> extends MekanismTileContainer<TILE> {
    private VirtualInventoryContainerSlot circuitSlot;

    public GMTileContainer(ContainerTypeRegistryObject<?> type, int id, Inventory inv, @NotNull TILE tile) {
        super(type, id, inv, tile);
    }

    @Override
    protected void addSlots() {
        super.addSlots();
        if (this.tile instanceof ITileProgrammable programmable) {
            this.addSlot(this.circuitSlot = programmable.getCircuitComponent().getCircuitSlot().createContainerSlot());
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotID) {
        Slot currentSlot = this.slots.get(slotID);
        if (currentSlot.hasItem()) {

            return super.quickMoveStack(player, slotID);
        } else {
            return ItemStack.EMPTY;
        }
    }

    public VirtualInventoryContainerSlot getCircuitSlot() {
        return circuitSlot;
    }
}
