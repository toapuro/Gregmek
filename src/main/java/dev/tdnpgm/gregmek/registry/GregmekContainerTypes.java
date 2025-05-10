package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.tile.TileEntityAssembler;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class GregmekContainerTypes {
    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(Gregmek.MODID);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityAssembler>> ASSEMBLER;

    static {
        ASSEMBLER = CONTAINER_TYPES.register("assembling_machine", TileEntityAssembler.class);
    }
}
