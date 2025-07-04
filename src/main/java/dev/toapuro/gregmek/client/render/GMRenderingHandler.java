package dev.toapuro.gregmek.client.render;

import dev.toapuro.gregmek.Gregmek;
import dev.toapuro.gregmek.client.GMSpecialColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Gregmek.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GMRenderingHandler {
    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Post event) {
        GMSpecialColors.GUI_OBJECTS.parse(Gregmek.rl("textures/colormap/gui_objects.png"));
    }
}
