package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.gui.GuiAlloySmelter;
import dev.tdnpgm.gregmek.gui.GuiAssembler;
import dev.tdnpgm.gregmek.gui.GuiBender;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Gregmek.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GMClientRegistration {
    public static <C extends AbstractContainerMenu, U extends Screen & MenuAccess<C>> void registerScreen(ContainerTypeRegistryObject<C> type, MenuScreens.ScreenConstructor<C, U> factory) {
        MenuScreens.register(type.get(), factory);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerContainers(RegisterEvent event) {
        event.register(Registries.MENU, helper -> {
            registerScreen(GMContainerTypes.ASSEMBLER, GuiAssembler::new);
            registerScreen(GMContainerTypes.ALLOY_SMELTER, GuiAlloySmelter::new);
            registerScreen(GMContainerTypes.BENDER, GuiBender::new);
        });
    }
}
