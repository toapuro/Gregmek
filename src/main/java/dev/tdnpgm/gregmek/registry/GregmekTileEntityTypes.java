package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.tile.TileEntityAssembler;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;

public class GregmekTileEntityTypes {
    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Gregmek.MODID);
    public static final TileEntityTypeRegistryObject<TileEntityAssembler> ASSEMBLING_MACHINE;

    static {
        ASSEMBLING_MACHINE = TILE_ENTITY_TYPES.register(GregmekBlocks.ASSEMBLING_MACHINE, TileEntityAssembler::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    }
}
