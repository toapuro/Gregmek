package dev.toapuro.gregmek.client.gui;

import dev.toapuro.gregmek.client.gui.element.tab.GuiCircuitConfigTab;
import dev.toapuro.gregmek.content.tile.TileEntityAssembler;
import dev.toapuro.gregmek.content.tile.container.GMTileContainer;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.inventory.warning.WarningTracker;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAssembler extends GMMachineGui<TileEntityAssembler, GMTileContainer<TileEntityAssembler>> {
    public GuiAssembler(GMTileContainer<TileEntityAssembler> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.dynamicSlots = true;
    }

    protected void addGuiElements() {
        super.addGuiElements();
        this.addRenderableWidget(new GuiVerticalPowerBar(this, this.tile.getEnergyContainer(), 164, 15)).warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY));
        MachineEnergyContainer<TileEntityAssembler> energyContainer = this.tile.getEnergyContainer();
        this.addRenderableWidget(new GuiEnergyTab(this, energyContainer, this.tile::getActive));
        this.addRenderableWidget((new GuiProgress(this.tile::getScaledProgress, ProgressType.BAR, this, 94, 38))
                        .jeiCategory(this.tile))
                .warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, this.tile.
                        getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
        this.addRenderableWidget(new GuiFluidGauge(() -> this.tile.inputFluidTank, () -> this.tile.getFluidTanks(null),
                        GaugeType.SMALL_MED, this, 70, 16))
                .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, this.tile
                        .getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT));
        this.addRenderableWidget(new GuiCircuitConfigTab<>(this, this.tile));
    }
}
