package dev.toapuro.gregmek;

import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.MekanismConfigHelper;
import mekanism.common.config.value.CachedDoubleValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CommonConfig extends BaseMekanismConfig {
    public static CommonConfig CONFIG = new CommonConfig();
    public CachedDoubleValue processingDurationMultiplier;
    private ForgeConfigSpec spec;

    public CommonConfig() {
        this.buildConfig();
    }

    public static void registerConfig(FMLJavaModLoadingContext modLoadingContext) {
        ModContainer modContainer = modLoadingContext.getContainer();
        MekanismConfigHelper.registerConfig(modContainer, CONFIG);
    }

    public void buildConfig() {
        CONFIG = this;

        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Machine processing time multiplier");
        processingDurationMultiplier = CachedDoubleValue.wrap(this, builder.define("processingDurationMultiplier", 2.0d));

        spec = builder.build();
    }

    @Override
    public String getFileName() {
        return "gregmek";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return spec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.COMMON;
    }
}
