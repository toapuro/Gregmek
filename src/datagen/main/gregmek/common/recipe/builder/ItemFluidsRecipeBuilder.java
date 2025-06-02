package gregmek.common.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.toapuro.gregmek.content.recipe.AssemblingRecipe;
import dev.toapuro.gregmek.content.recipe.BendingRecipe;
import gregmek.common.recipe.builder.abstracts.GMProcessingRecipeBuilder;
import gregmek.common.recipe.builder.abstracts.GMRecipeBuilder;
import mekanism.api.SerializerHelper;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NothingNullByDefault
public class ItemFluidsRecipeBuilder extends GMProcessingRecipeBuilder<ItemFluidsRecipeBuilder> {
    private final List<ItemStackIngredient> inputSolids;
    private List<FluidStackIngredient> inputFluids;
    private final int duration;
    private final ItemStack outputItem;
    private final int maxItemInputs;
    private final int maxFluidInputs;

    protected ItemFluidsRecipeBuilder(List<ItemStackIngredient> inputSolids, List<FluidStackIngredient> inputFluids, int maxItemInputs, int maxFluidInputs, int duration, ItemStack outputItem, ResourceLocation serializerName) {
        super(serializerName, false, FloatingLong.ZERO, duration);
        this.inputSolids = inputSolids;
        this.inputFluids = inputFluids;
        this.maxItemInputs = maxItemInputs;
        this.maxFluidInputs = maxFluidInputs;
        this.duration = duration;
        this.outputItem = outputItem;
    }

    public static ItemFluidsRecipeBuilder assembling(int duration, ItemStack outputItem) {
        if (outputItem.isEmpty()) {
            throw new IllegalArgumentException("This assembling recipe requires a non empty output item.");
        } else {
            validateDuration(duration);
            return new ItemFluidsRecipeBuilder(
                    new ArrayList<>(),
                    new ArrayList<>(),
                    AssemblingRecipe.MAX_ITEM_SLOTS,
                    AssemblingRecipe.MAX_FLUID_SLOTS,
                    duration,
                    outputItem,
                    gmSerializer("assembling"));
        }
    }

    public static ItemFluidsRecipeBuilder bending(int duration, ItemStack outputItem) {
        if (outputItem.isEmpty()) {
            throw new IllegalArgumentException("This bending recipe requires a non empty output item.");
        } else {
            validateDuration(duration);
            return new ItemFluidsRecipeBuilder(
                    new ArrayList<>(),
                    new ArrayList<>(),
                    BendingRecipe.MAX_ITEM_SLOTS,
                    BendingRecipe.MAX_FLUID_SLOTS,
                    duration,
                    outputItem,
                    gmSerializer("bending"));
        }
    }

    public ItemFluidsRecipeBuilder addItem(ItemStackIngredient ingredient) {
        this.inputSolids.add(ingredient);
        return this;
    }

    public ItemFluidsRecipeBuilder requireFluid(FluidStackIngredient ingredient) {
        this.inputFluids = Collections.singletonList(ingredient);
        return this;
    }

    private static void validateDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("This reaction recipe must have a positive duration.");
        }
    }

    @Override
    public void validate(ResourceLocation id) {
        if (duration <= 0) {
            throw new IllegalArgumentException("This reaction recipe must have a positive duration.");
        }

        if (this.inputSolids.size() > this.maxItemInputs) {
            throw new IllegalArgumentException("This too many input solids");
        }

        if (this.inputFluids.size() > this.maxFluidInputs) {
            throw new IllegalArgumentException("This too many input fluids");
        }
    }

    protected RecipeResult getResult(ResourceLocation id) {
        return new RecipeResult(id);
    }

    public class RecipeResult extends GMRecipeBuilder<ItemFluidsRecipeBuilder>.RecipeResult {
        protected RecipeResult(ResourceLocation id) {
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
            json.add("itemInputs", serializeIngredients(ItemFluidsRecipeBuilder.this.inputSolids));
            Optional<FluidStackIngredient> firstFluid = ItemFluidsRecipeBuilder.this.inputFluids.stream().findFirst();
            json.add("fluidInput", firstFluid.map(InputIngredient::serialize).orElseGet(JsonObject::new));

            json.addProperty("duration", ItemFluidsRecipeBuilder.this.duration);
            if (!ItemFluidsRecipeBuilder.this.outputItem.isEmpty()) {
                json.add("itemOutput", SerializerHelper.serializeItemStack(ItemFluidsRecipeBuilder.this.outputItem));
            }
        }
    }
}
