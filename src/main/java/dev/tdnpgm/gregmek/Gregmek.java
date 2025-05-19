package dev.tdnpgm.gregmek;

import com.mojang.logging.LogUtils;
import dev.tdnpgm.gregmek.registry.GregmekBlocks;
import dev.tdnpgm.gregmek.registry.GregmekContainerTypes;
import dev.tdnpgm.gregmek.registry.GregmekRecipeSerializers;
import dev.tdnpgm.gregmek.registry.GregmekTileEntityTypes;
import dev.tdnpgm.gregmek.registry.recipe.GregmekRecipeType;
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
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        Config.registerConfig();
        GregmekRecipeType.RECIPE_TYPES.register(modEventBus);
        GregmekBlocks.BLOCKS.register(modEventBus);
        GregmekTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        GregmekRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        GregmekContainerTypes.CONTAINER_TYPES.register(modEventBus);


        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
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
