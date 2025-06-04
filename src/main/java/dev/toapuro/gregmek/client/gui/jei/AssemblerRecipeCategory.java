package dev.toapuro.gregmek.client.gui.jei;

import dev.toapuro.gregmek.content.recipe.AssemblingRecipe;
import dev.toapuro.gregmek.content.registry.GMBlocks;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
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
    private final List<GuiSlot> inputSlots;
    private final GuiGauge<?> inputFluidGauge;
    private final GuiSlot output;

    public AssemblerRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<AssemblingRecipe> recipeType) {
        super(helper, recipeType, GMBlocks.ASSEMBLING_MACHINE, 18, 20, 144, 54);

        this.inputSlots = new ArrayList<>();
        for (int i = 0; i < AssemblingRecipe.MAX_ITEM_SLOTS; i++) {
            int slotXIndex = i % 3;
            int slotYIndex = i / 3;

            inputSlots.add(this.addSlot(SlotType.INPUT, 15 + 18 * (slotXIndex), 17 + 18 * slotYIndex));
        }

        this.output = this.addSlot(SlotType.OUTPUT, 130, 35);
        this.inputFluidGauge = this.addElement(GuiFluidGauge.getDummy(GaugeType.SMALL_MED.with(DataType.INPUT), this, 70, 16));
        this.addSlot(SlotType.POWER, 130, 56).with(SlotOverlay.POWER);
        this.addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        this.addSimpleProgress(ProgressType.BAR, 94, 38);
    }

    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, AssemblingRecipe recipe, @NotNull IFocusGroup focusGroup) {
        List<ItemStackIngredient> inputSolids = recipe.getInputSolids();
        for (int i = 0; i < inputSolids.size(); i++) {
            this.initItem(builder, RecipeIngredientRole.INPUT, this.inputSlots.get(i), inputSolids.get(i).getRepresentations());
        }
        List<FluidStackIngredient> inputFluids = recipe.getInputFluids();
        for (FluidStackIngredient inputFluid : inputFluids) {
            this.initFluid(builder, RecipeIngredientRole.INPUT, this.inputFluidGauge, inputFluid.getRepresentations());
        }
        this.initItem(builder, RecipeIngredientRole.OUTPUT, this.output, Collections.singletonList(recipe.getOutputDefinition()));
    }
}
