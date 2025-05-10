package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.tile.TileEntityAssembler;
import mekanism.api.Upgrade;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.Machine;

import java.util.EnumSet;

public class GregmekBlockTypes {
    public static final Machine<TileEntityAssembler> ASSENBLING_MACHINE;

    static {
        ASSENBLING_MACHINE = Machine.MachineBuilder.createMachine(() -> GregmekTileEntityTypes.ASSEMBLING_MACHINE, MekanismLang.DESCRIPTION_BIN)
                .withGui(() -> GregmekContainerTypes.ASSEMBLER)
                .withEnergyConfig(MekanismConfig.usage.chemicalDissolutionChamber, MekanismConfig.storage.chemicalDissolutionChamber)
                .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING))
                .build();
    }
}
