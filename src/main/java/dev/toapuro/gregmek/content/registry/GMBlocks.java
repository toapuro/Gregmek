package dev.toapuro.gregmek.content.registry;

import dev.toapuro.gregmek.Gregmek;
import dev.toapuro.gregmek.content.tile.TileEntityAlloySmelter;
import dev.toapuro.gregmek.content.tile.TileEntityAssembler;
import dev.toapuro.gregmek.content.tile.TileEntityBender;
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
    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityBender, Machine<TileEntityBender>>, ItemBlockMachine> BENDER;

    static {
        ASSEMBLING_MACHINE = BLOCKS.register("assembler", () -> new BlockTile.BlockTileModel<>(GMBlockTypes.ASSEMBLER,
                (properties) -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);
        ALLOY_SMELTER = BLOCKS.register("alloy_smelter", () -> new BlockTile.BlockTileModel<>(GMBlockTypes.ALLOY_SMELTER,
                (properties) -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);
        BENDER = BLOCKS.register("bender", () -> new BlockTile.BlockTileModel<>(GMBlockTypes.BENDER,
                (properties) -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);
    }
}
