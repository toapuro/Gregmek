package dev.tdnpgm.gregmek;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Config {
    public static ForgeConfigSpec SPEC;

    public static void registerConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        SPEC = builder.build();
    }
}
