package dev.toapuro.gregmek.content.tile.interfaces;

import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.base.TileEntityMekanism;

public interface ITileMachineEnergyContainer<TILE extends TileEntityMekanism> {
    MachineEnergyContainer<TILE> getMachineEnergyContainer();
}
