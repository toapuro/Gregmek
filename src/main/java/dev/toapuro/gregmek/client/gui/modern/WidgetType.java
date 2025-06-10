package dev.toapuro.gregmek.client.gui.modern;

import dev.toapuro.gregmek.client.gui.modern.context.GuiContext;
import mekanism.client.gui.IGuiWrapper;
import mekanism.common.tile.base.TileEntityMekanism;

public abstract class WidgetType<TILE extends TileEntityMekanism> {
    public abstract <T extends TILE> void applyToGui(GuiContext<T> context, int x, int y);

    public abstract void applyToJei(IGuiWrapper guiWrapper, int x, int y);
}
