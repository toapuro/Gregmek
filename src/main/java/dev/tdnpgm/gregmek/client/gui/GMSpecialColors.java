package dev.tdnpgm.gregmek.client.gui;

import mekanism.client.render.lib.ColorAtlas;

public class GMSpecialColors {
    public static final ColorAtlas GUI_OBJECTS = new ColorAtlas("gui_objects");
    public static final ColorAtlas.ColorRegistryObject TAB_CIRCUIT_TAB;

    static {
        TAB_CIRCUIT_TAB = GUI_OBJECTS.register(0x1F781F);
    }
}
