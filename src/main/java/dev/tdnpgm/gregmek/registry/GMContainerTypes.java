package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.tile.TileEntityAlloySmelter;
import dev.tdnpgm.gregmek.tile.TileEntityAssembler;
import dev.tdnpgm.gregmek.tile.TileEntityBender;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class GMContainerTypes {
    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(Gregmek.MODID);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityAssembler>> ASSEMBLER;
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityAlloySmelter>> ALLOY_SMELTER;
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityBender>> BENDER;

    static {
        ASSEMBLER = CONTAINER_TYPES.register("assembler", TileEntityAssembler.class);
        ALLOY_SMELTER = CONTAINER_TYPES.register("alloy_smelter", TileEntityAlloySmelter.class);
        BENDER = CONTAINER_TYPES.register("bender", TileEntityBender.class);
    }
}
