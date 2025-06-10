package dev.toapuro.gregmek;

import com.mojang.logging.LogUtils;
import dev.toapuro.gregmek.content.registry.*;
import dev.toapuro.gregmek.content.registry.recipe.GMRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;

@Mod(Gregmek.MODID)
public class Gregmek {

    public static final String MODID = "gregmek";
    public static final Logger DEBUG_LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public Gregmek() {
        FMLJavaModLoadingContext modLoadingContext = FMLJavaModLoadingContext.get();
        IEventBus modEventBus = modLoadingContext.getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        CommonConfig.registerConfig(modLoadingContext);
        GMRecipeType.RECIPE_TYPES.register(modEventBus);
        GMBlocks.BLOCKS.register(modEventBus);
        GMItems.ITEMS.register(modEventBus);
        GMTleEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        GMRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        GMContainerTypes.CONTAINER_TYPES.register(modEventBus);
        GMCreativeTabs.CREATIVE_TABS.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG.getConfigSpec());
    }

    @Contract(
            value = "_ -> new",
            pure = true
    )
    @SuppressWarnings("removal")
    public static ResourceLocation rl(String path) {
        return new ResourceLocation(Gregmek.MODID, path);
    }
}
