package dev.toapuro.gregmek;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static ForgeConfigSpec SPEC;

    public static void registerConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        SPEC = builder.build();
    }
}
