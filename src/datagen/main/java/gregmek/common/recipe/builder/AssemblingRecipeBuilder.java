package java.gregmek.common.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mekanism.api.SerializerHelper;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NothingNullByDefault
public class AssemblingRecipeBuilder extends GMRecipeBuilder<AssemblingRecipeBuilder> {
    private final List<ItemStackIngredient> inputSolids;
    private List<FluidStackIngredient> inputFluids;
    private final int duration;
    private final ItemStack outputItem;

    protected AssemblingRecipeBuilder(List<ItemStackIngredient> inputSolids, List<FluidStackIngredient> inputFluids, int duration, ItemStack outputItem) {
        super(mekSerializer("assembling"), false, FloatingLong.ZERO);
        this.inputSolids = inputSolids;
        this.inputFluids = inputFluids;
        this.duration = duration;
        this.outputItem = outputItem;
    }

    public static AssemblingRecipeBuilder assembling(List<ItemStackIngredient> inputSolid, FluidStackIngredient inputFluid, int duration, ItemStack outputItem) {
        if (outputItem.isEmpty()) {
            throw new IllegalArgumentException("This assembling recipe requires a non empty output item.");
        } else {
            validateDuration(duration);
            return new AssemblingRecipeBuilder(inputSolid, Collections.singletonList(inputFluid), duration, outputItem);
        }
    }

    public static AssemblingRecipeBuilder assembling(List<ItemStackIngredient> inputSolid, int duration, ItemStack outputItem) {
        if (outputItem.isEmpty()) {
            throw new IllegalArgumentException("This assembling recipe requires a non empty output item.");
        } else {
            validateDuration(duration);
            return new AssemblingRecipeBuilder(inputSolid, Collections.emptyList(), duration, outputItem);
        }
    }

    public static AssemblingRecipeBuilder assembling(int duration, ItemStack outputItem) {
        if (outputItem.isEmpty()) {
            throw new IllegalArgumentException("This assembling recipe requires a non empty output item.");
        } else {
            validateDuration(duration);
            return new AssemblingRecipeBuilder(Collections.emptyList(), Collections.emptyList(), duration, outputItem);
        }
    }

    public AssemblingRecipeBuilder addItem(ItemStackIngredient ingredient) {
        this.inputSolids.add(ingredient);
        return this;
    }

    public AssemblingRecipeBuilder requireFluid(FluidStackIngredient ingredient) {
        this.inputFluids = Collections.singletonList(ingredient);
        return this;
    }

    private static void validateDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("This reaction recipe must have a positive duration.");
        }
    }

    protected AssemblingResult getResult(ResourceLocation id) {
        return new AssemblingResult(id);
    }

    public class AssemblingResult extends GMRecipeBuilder<AssemblingRecipeBuilder>.RecipeResult {
        protected AssemblingResult(ResourceLocation id) {
            super(id);
        }

        public <T> JsonArray serializeIngredients(List<? extends InputIngredient<T>> serializable) {
            JsonArray result = new JsonArray();
            for (InputIngredient<?> inputIngredient : serializable) {
                result.add(inputIngredient.serialize());
            }
            return result;
        }

        public void serializeRecipeData(@NotNull JsonObject json) {
            json.add("itemInputs", serializeIngredients(AssemblingRecipeBuilder.this.inputSolids));
            Optional<FluidStackIngredient> firstFluid = AssemblingRecipeBuilder.this.inputFluids.stream().findFirst();
            json.add("fluidInput", firstFluid.map(InputIngredient::serialize).orElseGet(JsonObject::new));

            json.addProperty("duration", AssemblingRecipeBuilder.this.duration);
            if (!AssemblingRecipeBuilder.this.outputItem.isEmpty()) {
                json.add("itemOutput", SerializerHelper.serializeItemStack(AssemblingRecipeBuilder.this.outputItem));
            }
        }
    }
}
