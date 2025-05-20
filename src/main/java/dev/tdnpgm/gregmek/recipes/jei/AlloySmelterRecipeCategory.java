package dev.tdnpgm.gregmek.recipes.jei;

import dev.tdnpgm.gregmek.recipes.AlloySmelterRecipe;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.registries.MekanismBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import org.jetbrains.annotations.NotNull;

public class AlloySmelterRecipeCategory extends BaseRecipeCategory<AlloySmelterRecipe> {
    private final GuiSlot input;
    private final GuiSlot secondary;
    private final GuiSlot output;

    public AlloySmelterRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<AlloySmelterRecipe> recipeType) {
        super(helper, recipeType, MekanismBlocks.COMBINER, 28, 16, 144, 54);
        this.addElement(new GuiUpArrow(this, 68, 38));
        this.input = this.addSlot(SlotType.INPUT, 64, 17);
        this.secondary = this.addSlot(SlotType.EXTRA, 64, 53);
        this.output = this.addSlot(SlotType.OUTPUT, 116, 35);
        this.addSlot(SlotType.POWER, 39, 35).with(SlotOverlay.POWER);
        this.addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        this.addSimpleProgress(ProgressType.BAR, 86, 38);
    }

    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, AlloySmelterRecipe recipe, @NotNull IFocusGroup focusGroup) {
        this.initItem(builder, RecipeIngredientRole.INPUT, this.input, recipe.getMainInput().getRepresentations());
        this.initItem(builder, RecipeIngredientRole.INPUT, this.secondary, recipe.getSecondaryInput().getRepresentations());
        this.initItem(builder, RecipeIngredientRole.OUTPUT, this.output, recipe.getOutputDefinition());
    }
}
