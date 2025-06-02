package dev.toapuro.gregmek.client.gui;

import mekanism.client.gui.GuiConfigurableTile;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.ISideConfiguration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GMConfigurableTile<TILE extends TileEntityMekanism & ISideConfiguration, CONTAINER extends MekanismTileContainer<TILE>>
        extends GuiConfigurableTile<TILE, CONTAINER> {
    protected GMConfigurableTile(CONTAINER container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void addSlots() {
        super.addSlots();
    }
}
