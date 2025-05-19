package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.tile.TileEntityAssembler;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.resource.BlockResourceInfo;

public class GregmekBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Gregmek.MODID);
    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityAssembler, Machine<TileEntityAssembler>>, ItemBlockMachine> ASSEMBLING_MACHINE;

    static {
        ASSEMBLING_MACHINE = BLOCKS.register("assembler", () -> new BlockTile.BlockTileModel<>(GregmekBlockTypes.ASSENBLING_MACHINE, (properties) -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);
    }
}
