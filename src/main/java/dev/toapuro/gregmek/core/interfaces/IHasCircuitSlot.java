package dev.toapuro.gregmek.core.interfaces;

import mekanism.common.inventory.container.slot.VirtualInventoryContainerSlot;

public interface IHasCircuitSlot {
    VirtualInventoryContainerSlot gregmek$getCircuitSlot();

    void gregmek$setCircuitSlot(VirtualInventoryContainerSlot circuitSlot);
}
