package dev.tdnpgm.gregmek.content.tile.inventory;

import dev.tdnpgm.gregmek.content.item.ProgrammedCircuit;
import mekanism.api.IContentsListener;
import mekanism.common.inventory.container.SelectedWindowData;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.slot.VirtualInventoryContainerSlot;
import mekanism.common.inventory.slot.BasicInventorySlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CircuitVirtualInventorySlot extends BasicInventorySlot {
    private CircuitVirtualInventorySlot(@Nullable IContentsListener listener) {
        super(
                manualOnly,
                alwaysTrueBi,
                (stack) -> stack.getItem() instanceof ProgrammedCircuit,
                listener, 0, 0);
        this.setSlotOverlay(SlotOverlay.MODULE);
    }

    public static CircuitVirtualInventorySlot input(@Nullable IContentsListener listener) {
        return new CircuitVirtualInventorySlot(listener);
    }

    public @NotNull VirtualInventoryContainerSlot createContainerSlot() {
        return new VirtualInventoryContainerSlot(this,
                new SelectedWindowData(SelectedWindowData.WindowType.UNSPECIFIED),
                this.getSlotOverlay(),
                this::setStackUnchecked);
    }
}
