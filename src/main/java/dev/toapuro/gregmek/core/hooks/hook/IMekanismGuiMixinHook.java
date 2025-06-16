package dev.toapuro.gregmek.core.hooks.hook;

import dev.toapuro.gregmek.core.hooks.IMixinHook;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiElement;

import java.util.function.Consumer;

public interface IMekanismGuiMixinHook extends IMixinHook {
    default void addGuiElements(GuiMekanismTile<?, ?> mekanismTile, Consumer<GuiElement> addElementConsumer) {
    }
}
