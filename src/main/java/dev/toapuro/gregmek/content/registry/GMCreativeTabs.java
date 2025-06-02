package dev.toapuro.gregmek.content.registry;

import dev.toapuro.gregmek.Gregmek;
import dev.toapuro.gregmek.content.lang.GregmekLang;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.CreativeTabRegistryObject;

public class GMCreativeTabs {
    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(Gregmek.MODID);
    public static final CreativeTabRegistryObject GREGMEK;


    static {
        GREGMEK = CREATIVE_TABS.registerMain(GregmekLang.GREGMEK, GMBlocks.ASSEMBLING_MACHINE,
                (builder) -> builder.withSearchBar().displayItems((displayParameters, output) -> {
                    CreativeTabDeferredRegister.addToDisplay(GMBlocks.BLOCKS, output);
                    CreativeTabDeferredRegister.addToDisplay(GMItems.ITEMS, output);
                }));
    }
}
