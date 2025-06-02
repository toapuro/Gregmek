package dev.toapuro.gregmek.content.tile.component;

import dev.toapuro.gregmek.common.api.GMNBTConstants;
import dev.toapuro.gregmek.content.tile.inventory.CircuitVirtualInventorySlot;
import mekanism.api.DataHandlerUtils;
import mekanism.api.NBTConstants;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.util.NBTUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.Collections;

public class TileComponentCircuit implements ITileComponent {
    private final CircuitVirtualInventorySlot circuitSlot;

    public TileComponentCircuit(TileEntityMekanism tile) {
        this.circuitSlot = CircuitVirtualInventorySlot.input(tile);
        tile.addComponent(this);
    }

    @Override
    public void read(CompoundTag nbtTags) {
        NBTUtils.setCompoundIfPresent(nbtTags, GMNBTConstants.COMPONENT_CIRCUIT, circuitNBT -> {
            NBTUtils.setListIfPresent(circuitNBT, NBTConstants.ITEMS, Tag.TAG_COMPOUND, list ->
                    DataHandlerUtils.readContainers(Collections.singletonList(circuitSlot), list));
        });
    }

    @Override
    public void write(CompoundTag nbtTags) {
        CompoundTag upgradeNBT = new CompoundTag();
        upgradeNBT.put(NBTConstants.ITEMS, DataHandlerUtils.writeContainers(Collections.singletonList(circuitSlot)));
        nbtTags.put(GMNBTConstants.COMPONENT_CIRCUIT, upgradeNBT);
    }

    public CircuitVirtualInventorySlot getCircuitSlot() {
        return circuitSlot;
    }
}
