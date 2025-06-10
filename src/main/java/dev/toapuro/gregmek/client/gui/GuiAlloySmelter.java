package dev.toapuro.gregmek.client.gui;

import dev.toapuro.gregmek.client.gui.element.tab.GuiCircuitConfigTab;
import dev.toapuro.gregmek.content.tile.TileEntityAlloySmelter;
import dev.toapuro.gregmek.content.tile.container.GMTileContainer;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.inventory.warning.WarningTracker;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;


public class GuiAlloySmelter extends GMMachineGui<TileEntityAlloySmelter, GMTileContainer<TileEntityAlloySmelter>> {
    public GuiAlloySmelter(GMTileContainer<TileEntityAlloySmelter> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.dynamicSlots = true;
    }

    protected void addGuiElements() {
        super.addGuiElements();

        this.addRenderableWidget(new GuiVerticalPowerBar(this, this.tile.getEnergyContainer(), 164, 15))
                .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY));
        MachineEnergyContainer<TileEntityAlloySmelter> energyContainer = this.tile.getEnergyContainer();
        this.addRenderableWidget(new GuiEnergyTab(this, energyContainer, this.tile::getActive));
        this.addRenderableWidget((new GuiProgress(this.tile::getScaledProgress, ProgressType.RIGHT, this, 79, 38)).jeiCategory(this.tile))
                .warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
        this.addRenderableWidget(new GuiCircuitConfigTab<>(this, this.tile));
    }
}
