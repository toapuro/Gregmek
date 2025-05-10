package dev.tdnpgm.gregmek.recipes;

import dev.tdnpgm.gregmek.utils.GregmekUtils;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.integration.projecte.IngredientHelper;
import mekanism.common.recipe.ingredient.creator.ItemStackIngredientCreator;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.extensions.IForgeIntrinsicHolderTagAppender;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

public abstract class AssemblingRecipe extends MekanismRecipe implements BiPredicate<List<ItemStack>, List<FluidStack>> {
    private final List<ItemStackIngredient> inputSolids;
    private final FluidStackIngredient inputFluid;
    private final List<FluidStackIngredient> inputFluids;
    private final FloatingLong energyRequired;
    private final int duration;
    private final ItemStack outputItem;

    public static int itemSlots = 9;
    public static int fluidSlots = 1;

    public AssemblingRecipe(ResourceLocation id, List<ItemStackIngredient> inputSolids, FluidStackIngredient inputFluid, FloatingLong energyRequired, int duration, ItemStack outputItem) {
        super(id);

        Objects.requireNonNull(inputSolids, "Item input cannot be null.");
        this.inputSolids = inputSolids;
        this.inputFluid = Objects.requireNonNull(inputFluid, "Fluid input cannot be null.");
        this.inputFluids = Collections.singletonList(inputFluid);
        this.energyRequired = Objects.requireNonNull(energyRequired, "Required energy cannot be null.").copyAsConst();
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
    public FluidStackIngredient getInputFluid() {
        return this.inputFluid;
    }

    public FloatingLong getEnergyRequired() {
        return this.energyRequired;
    }

    public int getDuration() {
        return this.duration;
    }

    public boolean test(List<ItemStack> solids, List<FluidStack> liquid) {
        for (ItemStack itemStack : solids) {
            if (inputSolids.stream().noneMatch(itemStackIngredient -> itemStackIngredient.testType(itemStack))) {
                return false;
            }
        }

        return true;
//        Optional<FluidStack> firstFluid = liquid.stream().findFirst();
//        return firstFluid.filter(inputFluid::testType).isPresent();

    }

    public AssemblingRecipeOutput getOutputDefinition() {
        return new AssemblingRecipeOutput(this.outputItem);
    }

    @Contract(
            value = "_, _ -> new",
            pure = true
    )
    public AssemblingRecipeOutput getOutput(List<ItemStack> solid, FluidStack liquid) {
        return new AssemblingRecipeOutput(this.outputItem.copy());
    }

    public boolean isIncomplete() {
        return this.inputSolids.stream().anyMatch(InputIngredient::hasNoMatchingInstances) || this.inputFluid.hasNoMatchingInstances();
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(inputSolids.size());
        for (ItemStackIngredient inputSolid : inputSolids) {
            inputSolid.write(buffer);
        }
        this.inputFluid.write(buffer);
        this.energyRequired.writeToBuffer(buffer);
        buffer.writeVarInt(this.duration);
        buffer.writeItem(this.outputItem);
    }

    public static record AssemblingRecipeOutput(@NotNull ItemStack item) {
        public AssemblingRecipeOutput(@NotNull ItemStack item) {
            Objects.requireNonNull(item, "Item output cannot be null.");
            if (item.isEmpty()) {
                throw new IllegalArgumentException("Item output cannot be present.");
            } else {
                this.item = item;
            }
        }

        public @NotNull ItemStack item() {
            return this.item;
        }
    }
}
