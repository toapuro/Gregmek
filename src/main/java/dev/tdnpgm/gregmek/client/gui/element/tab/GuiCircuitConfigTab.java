package dev.tdnpgm.gregmek.client.gui.element.tab;

import dev.tdnpgm.gregmek.client.gui.GMSpecialColors;
import dev.tdnpgm.gregmek.content.tile.container.GMTileContainer;
import dev.tdnpgm.gregmek.content.tile.interfaces.ITileProgrammable;
import mekanism.client.gui.GuiMekanism;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiSideHolder;
import mekanism.client.gui.element.slot.GuiVirtualSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.client.gui.GuiGraphics;

public class GuiCircuitConfigTab<TILE extends TileEntityConfigurableMachine & ITileProgrammable> extends GuiSideHolder implements IGuiWrapper {
    private final TILE tile;

    @SuppressWarnings("unchecked")
    public GuiCircuitConfigTab(IGuiWrapper gui, TILE tile) {
        super(gui, gui.getWidth(), 62, 26, false, true);
        this.tile = tile;

        GMTileContainer<TILE> container = (GMTileContainer<TILE>) ((GuiMekanism<?>) gui()).getMenu();
        addChild(new GuiVirtualSlot(null, SlotType.NORMAL, gui, relativeX + (width - 18) / 2, relativeY + (height - 18) / 2, container.getCircuitSlot()));
    }

    @Override
    protected void colorTab(GuiGraphics guiGraphics) {
        MekanismRenderer.color(guiGraphics, GMSpecialColors.TAB_CIRCUIT_TAB);
    }
}