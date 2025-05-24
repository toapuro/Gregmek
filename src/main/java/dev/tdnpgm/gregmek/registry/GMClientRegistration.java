package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.gui.GuiAlloySmelter;
import dev.tdnpgm.gregmek.gui.GuiAssembler;
import dev.tdnpgm.gregmek.gui.GuiBender;
import mekanism.client.ClientRegistrationUtil;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Gregmek.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GMClientRegistration {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerContainers(RegisterEvent event) {
        event.register(Registries.MENU, helper -> {
            ClientRegistrationUtil.registerScreen(GMContainerTypes.ASSEMBLER, GuiAssembler::new);
            ClientRegistrationUtil.registerScreen(GMContainerTypes.ALLOY_SMELTER, GuiAlloySmelter::new);
            ClientRegistrationUtil.registerScreen(GMContainerTypes.BENDER, GuiBender::new);
        });
    }
}
