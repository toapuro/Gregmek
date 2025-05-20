package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.tile.TileEntityAlloySmelter;
import dev.tdnpgm.gregmek.tile.TileEntityAssembler;
import mekanism.api.Upgrade;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.Machine;

import java.util.EnumSet;

public class GMBlockTypes {
    public static final Machine<TileEntityAssembler> ASSENBLING_MACHINE;
    public static final Machine<TileEntityAlloySmelter> ALLOY_SMELTER;

    static {
        ASSENBLING_MACHINE = Machine.MachineBuilder.createMachine(() -> GregmekTileEntityTypes.ASSEMBLING_MACHINE, MekanismLang.DESCRIPTION_BIN)
                .withGui(() -> GMContainerTypes.ASSEMBLER)
                .withEnergyConfig(MekanismConfig.usage.chemicalDissolutionChamber, MekanismConfig.storage.chemicalDissolutionChamber)
                .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING))
                .build();
        ALLOY_SMELTER = Machine.MachineBuilder.createMachine(() -> GregmekTileEntityTypes.ALLOY_SMELTER, MekanismLang.DESCRIPTION_BIN)
                .withGui(() -> GMContainerTypes.ALLOY_SMELTER)
                .withEnergyConfig(MekanismConfig.usage.chemicalDissolutionChamber, MekanismConfig.storage.chemicalDissolutionChamber)
                .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING))
                .build();
    }
}
