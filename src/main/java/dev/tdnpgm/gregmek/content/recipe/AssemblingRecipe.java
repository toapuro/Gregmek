package dev.tdnpgm.gregmek.content.recipe;

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

        Objects.requireNonNull(inputSolids, "Item input cannot be null.");
        this.inputSolids = inputSolids;
        this.inputFluids = Objects.requireNonNull(inputFluids, "Fluid input cannot be null.");
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive.");
        } else {
            this.duration = duration;
            Objects.requireNonNull(outputItem, "Item output cannot be null.");
            if (outputItem.isEmpty()) {
                throw new IllegalArgumentException("At least one output must not be empty.");
            } else {
                Objects.requireNonNull(outputItem, "Item output cannot be null.");
                if (outputItem.isEmpty()) {
                    throw new IllegalArgumentException("At least one output must not be empty.");
                } else {
                    this.outputItem = outputItem.copy();
                }
            }
        }
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

    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarInt(inputSolids.size());
        for (ItemStackIngredient inputSolid : inputSolids) {
            inputSolid.write(buffer);
        }

        buffer.writeVarInt(inputFluids.size());
        for (FluidStackIngredient inputFluid : inputFluids) {
            inputFluid.write(buffer);
        }

        buffer.writeVarInt(this.duration);
        buffer.writeItem(this.outputItem);
    }
}
