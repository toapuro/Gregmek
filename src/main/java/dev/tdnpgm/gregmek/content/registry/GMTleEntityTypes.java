package dev.tdnpgm.gregmek.content.registry;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.content.tile.TileEntityAlloySmelter;
import dev.tdnpgm.gregmek.content.tile.TileEntityAssembler;
import dev.tdnpgm.gregmek.content.tile.TileEntityBender;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;

public class GMTleEntityTypes {
    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Gregmek.MODID);
    public static final TileEntityTypeRegistryObject<TileEntityAssembler> ASSEMBLING_MACHINE;
    public static final TileEntityTypeRegistryObject<TileEntityAlloySmelter> ALLOY_SMELTER;
    public static final TileEntityTypeRegistryObject<TileEntityBender> BENDER;

    static {
        ASSEMBLING_MACHINE = TILE_ENTITY_TYPES.register(GMBlocks.ASSEMBLING_MACHINE, TileEntityAssembler::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
        ALLOY_SMELTER = TILE_ENTITY_TYPES.register(GMBlocks.ALLOY_SMELTER, TileEntityAlloySmelter::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
        BENDER = TILE_ENTITY_TYPES.register(GMBlocks.BENDER, TileEntityBender::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    }
}
