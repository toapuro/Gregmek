package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.tile.TileEntityAlloySmelter;
import dev.tdnpgm.gregmek.tile.TileEntityAssembler;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.resource.BlockResourceInfo;

public class GMBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Gregmek.MODID);
    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityAssembler, Machine<TileEntityAssembler>>, ItemBlockMachine> ASSEMBLING_MACHINE;
    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityAlloySmelter, Machine<TileEntityAlloySmelter>>, ItemBlockMachine> ALLOY_SMELTER;

    static {
        ASSEMBLING_MACHINE = BLOCKS.register("assembler", () -> new BlockTile.BlockTileModel<>(GMBlockTypes.ASSENBLING_MACHINE,
                (properties) -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);
        ALLOY_SMELTER = BLOCKS.register("alloy_smelter", () -> new BlockTile.BlockTileModel<>(GMBlockTypes.ALLOY_SMELTER,
                (properties) -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);
    }
}
