package dev.tdnpgm.gregmek.content.recipe.jei;

import dev.tdnpgm.gregmek.content.recipe.BendingRecipe;
import dev.tdnpgm.gregmek.content.registry.GMBlocks;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BenderRecipeCategory extends BaseRecipeCategory<BendingRecipe> {
    private final GuiSlot mainSlot;
    private final GuiSlot extraSlot;
    private final GuiSlot output;

    public BenderRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<BendingRecipe> recipeType) {
        super(helper, recipeType, GMBlocks.BENDER, 18, 20, 144, 54);
        this.mainSlot = this.addSlot(SlotType.INPUT, 25, 35);
        this.extraSlot = this.addSlot(SlotType.INPUT, 43, 35);
        this.output = this.addSlot(SlotType.OUTPUT, 124, 35);
        this.addSlot(SlotType.POWER, 124, 56).with(SlotOverlay.POWER);
        this.addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        this.addSimpleProgress(ProgressType.BAR, 79, 38);
    }

    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, BendingRecipe recipe, @NotNull IFocusGroup focusGroup) {
        List<GuiSlot> inputSlots = List.of(mainSlot, extraSlot);
        for (int i = 0; i < recipe.getInputSolids().size(); i++) {
            this.initItem(builder, RecipeIngredientRole.INPUT, inputSlots.get(i), recipe.getInputSolids().get(i).getRepresentations());
        }
        this.initItem(builder, RecipeIngredientRole.OUTPUT, this.output, recipe.getOutputDefinition());
    }
}
