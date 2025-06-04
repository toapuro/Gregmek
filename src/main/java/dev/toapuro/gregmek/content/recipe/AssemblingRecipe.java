package dev.toapuro.gregmek.content.recipe;

import dev.toapuro.gregmek.common.api.exceptions.GMRecipeError;
import dev.toapuro.gregmek.common.helper.RecipeHelper;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

public abstract class AssemblingRecipe extends MekanismRecipe implements BiPredicate<List<ItemStack>, List<FluidStack>> {
    private final List<ItemStackIngredient> inputSolids;
    private final List<FluidStackIngredient> inputFluids;
    private final int duration;
    private final ItemStack outputItem;

    public static int MAX_ITEM_SLOTS = 9;
    public static int MAX_FLUID_SLOTS = 1;

    public AssemblingRecipe(ResourceLocation id, List<ItemStackIngredient> inputSolids, List<FluidStackIngredient> inputFluids, int duration, ItemStack outputItem) {
        super(id);

        this.inputSolids = Objects.requireNonNull(inputSolids, GMRecipeError.INPUT_NULL.getMessage());
        this.inputFluids = Objects.requireNonNull(inputFluids, GMRecipeError.INPUT_NULL.getMessage());
        this.duration = RecipeHelper.validateDuration(duration);
        this.outputItem = RecipeHelper.requireNotEmpty(outputItem, GMRecipeError.OUTPUT_NULL, GMRecipeError.OUTPUT_EMPTY).copy();
    }

    public List<ItemStackIngredient> getInputSolids() {
        return this.inputSolids;
    }

    public List<FluidStackIngredient> getInputFluids() {
        return this.inputFluids;
    }

    public int getDuration() {
        return this.duration;
    }

    public boolean test(List<ItemStack> solids, List<FluidStack> liquid) {
        for (ItemStackIngredient inputSolid : inputSolids) {
            if (solids.stream().noneMatch(inputSolid)) {
                return false;
            }
        }

        for (FluidStackIngredient inputFluid : inputFluids) {
            if (liquid.stream().noneMatch(inputFluid)) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getOutputDefinition() {
        return this.outputItem;
    }

    @Contract(
            value = "-> new",
            pure = true
    )
    public ItemStack getOutput() {
        return this.outputItem.copy();
    }

    public boolean isIncomplete() {
        return false;
//        return this.inputSolids.stream().anyMatch(InputIngredient::hasNoMatchingInstances) || this.inputFluid.hasNoMatchingInstances();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {

    }
}
