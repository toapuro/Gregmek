package dev.tdnpgm.gregmek.recipes.jei;

import dev.tdnpgm.gregmek.recipes.AlloySmeltingRecipe;
import dev.tdnpgm.gregmek.registry.GMBlocks;
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

import java.util.List;

public class AlloySmelterRecipeCategory extends BaseRecipeCategory<AlloySmeltingRecipe> {
    private final GuiSlot input;
    private final GuiSlot secondary;
    private final GuiSlot output;

    public AlloySmelterRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<AlloySmeltingRecipe> recipeType) {
        super(helper, recipeType, GMBlocks.ALLOY_SMELTER, 28, 16, 144, 54);
        this.addElement(new GuiUpArrow(this, 68, 38));
        this.input = this.addSlot(SlotType.INPUT, 64, 17);
        this.secondary = this.addSlot(SlotType.EXTRA, 64, 53);
        this.output = this.addSlot(SlotType.OUTPUT, 116, 35);
        this.addSlot(SlotType.POWER, 39, 35).with(SlotOverlay.POWER);
        this.addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        this.addSimpleProgress(ProgressType.BAR, 79, 38);
    }

    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, AlloySmeltingRecipe recipe, @NotNull IFocusGroup focusGroup) {
        List<GuiSlot> inputSlots = List.of(input, secondary);
        for (int i = 0; i < recipe.getInputSolids().size(); i++) {
            this.initItem(builder, RecipeIngredientRole.INPUT, inputSlots.get(i), recipe.getInputSolids().get(i).getRepresentations());
        }
        this.initItem(builder, RecipeIngredientRole.OUTPUT, this.output, recipe.getOutputDefinition());
    }
}
