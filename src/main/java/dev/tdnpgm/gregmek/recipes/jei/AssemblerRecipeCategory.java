package dev.tdnpgm.gregmek.recipes.jei;

import dev.tdnpgm.gregmek.recipes.AssemblingRecipe;
import dev.tdnpgm.gregmek.registry.GregmekBlocks;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssemblerRecipeCategory extends BaseRecipeCategory<AssemblingRecipe> {
    private final List<GuiSlot> inputs;
    private final GuiGauge<?> inputFluid;
    private final GuiSlot output;

    public AssemblerRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<AssemblingRecipe> recipeType) {
        super(helper, recipeType, GregmekBlocks.ASSEMBLING_MACHINE, 28, 16, 144, 54);
        this.addElement(new GuiUpArrow(this, 68, 38));

        this.inputs = new ArrayList<>();
        for (int i = 0; i < AssemblingRecipe.itemSlots; i++) {
            int slotXIndex = i % 3;
            int slotYIndex = i / 3;

            inputs.add(this.addSlot(SlotType.INPUT, 15+18* (slotXIndex), 17+18*slotYIndex));
        }

        this.output = this.addSlot(SlotType.OUTPUT, 130, 35);
        this.inputFluid = this.addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 45, 17));
        this.addSlot(SlotType.POWER, 39, 35).with(SlotOverlay.POWER);
        this.addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        this.addSimpleProgress(ProgressType.BAR, 100, 38);
    }

    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, AssemblingRecipe recipe, @NotNull IFocusGroup focusGroup) {
        List<ItemStackIngredient> inputSolids = recipe.getInputSolids();
        for (int i = 0; i < inputSolids.size(); i++) {
            this.initItem(builder, RecipeIngredientRole.INPUT, this.inputs.get(i), inputSolids.get(i).getRepresentations());
        }
        this.initFluid(builder, RecipeIngredientRole.INPUT, this.inputFluid, recipe.getInputFluid().getRepresentations());
        this.initItem(builder, RecipeIngredientRole.OUTPUT, this.output, Collections.singletonList(recipe.getOutputDefinition()));
    }
}
