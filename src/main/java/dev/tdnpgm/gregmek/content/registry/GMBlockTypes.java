package dev.tdnpgm.gregmek.content.registry;

import dev.tdnpgm.gregmek.content.tile.TileEntityAlloySmelter;
import dev.tdnpgm.gregmek.content.tile.TileEntityAssembler;
import dev.tdnpgm.gregmek.content.tile.TileEntityBender;
import mekanism.api.Upgrade;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.Machine;

import java.util.EnumSet;

public class GMBlockTypes {
    public static final Machine<TileEntityAssembler> ASSEMBLER;
    public static final Machine<TileEntityAlloySmelter> ALLOY_SMELTER;
    public static final Machine<TileEntityBender> BENDER;

    static {
        ASSEMBLER = Machine.MachineBuilder.createMachine(() -> GMTleEntityTypes.ASSEMBLING_MACHINE, MekanismLang.DESCRIPTION_BIN)
                .withGui(() -> GMContainerTypes.ASSEMBLER)
                .withEnergyConfig(MekanismConfig.usage.chemicalDissolutionChamber, MekanismConfig.storage.chemicalDissolutionChamber)
                .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING))
                .build();
        ALLOY_SMELTER = Machine.MachineBuilder.createMachine(() -> GMTleEntityTypes.ALLOY_SMELTER, MekanismLang.DESCRIPTION_BIN)
                .withGui(() -> GMContainerTypes.ALLOY_SMELTER)
                .withEnergyConfig(MekanismConfig.usage.chemicalDissolutionChamber, MekanismConfig.storage.chemicalDissolutionChamber)
                .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING))
                .build();
        BENDER = Machine.MachineBuilder.createMachine(() -> GMTleEntityTypes.BENDER, MekanismLang.DESCRIPTION_BIN)
                .withGui(() -> GMContainerTypes.BENDER)
                .withEnergyConfig(MekanismConfig.usage.chemicalDissolutionChamber, MekanismConfig.storage.chemicalDissolutionChamber)
                .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING))
                .build();
    }
}
