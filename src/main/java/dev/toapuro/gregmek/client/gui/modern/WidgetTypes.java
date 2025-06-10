package dev.toapuro.gregmek.client.gui.modern;

import dev.toapuro.gregmek.client.gui.modern.context.GuiContext;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;

public class WidgetTypes {
    private final WidgetType<TileEntityRecipeMachine<?>> RECIPE_VERTICAL_POWER_BAR = new WidgetType<TileEntityRecipeMachine<?>>() {
        @Override
        public <T extends TileEntityRecipeMachine<?>> void applyToGui(GuiContext<T> context, int x, int y) {
            IEnergyContainer energyContainer = context.tile().getEnergyContainer(0, null);
            context.addElement(new GuiVerticalPowerBar(context.gui(), energyContainer, x, y))
                    .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, context.tile().getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY));
        }

        @Override
        public void applyToJei(IGuiWrapper guiWrapper, int x, int y) {

        }
    };
}
