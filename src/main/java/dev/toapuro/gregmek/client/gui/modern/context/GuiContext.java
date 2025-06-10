package dev.toapuro.gregmek.client.gui.modern.context;

import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiElement;

import java.util.function.Consumer;

public class GuiContext<TILE> {
    private final IGuiWrapper guiWrapper;
    private final Consumer<GuiElement> addElementConsumer;
    private final TILE tile;

    public GuiContext(IGuiWrapper guiWrapper, Consumer<GuiElement> addElementConsumer, TILE tile) {
        this.guiWrapper = guiWrapper;
        this.addElementConsumer = addElementConsumer;
        this.tile = tile;
    }

    public <T extends GuiElement> T addElement(T t) {
        this.addElementConsumer.accept(t);
        return t;
    }

    public IGuiWrapper gui() {
        return guiWrapper;
    }

    public TILE tile() {
        return tile;
    }
}
