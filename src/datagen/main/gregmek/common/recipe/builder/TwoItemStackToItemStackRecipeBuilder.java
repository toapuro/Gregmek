package gregmek.common.recipe.builder;

import com.google.gson.JsonObject;
import gregmek.common.recipe.builder.abstracts.GMProcessingRecipeBuilder;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TwoItemStackToItemStackRecipeBuilder extends GMProcessingRecipeBuilder<TwoItemStackToItemStackRecipeBuilder> {
    private final ItemStackIngredient mainInput;
    private final ItemStackIngredient extraInput;
    private final ItemStack output;

    protected TwoItemStackToItemStackRecipeBuilder(int duration, ItemStackIngredient mainInput, ItemStackIngredient extraInput, ItemStack output, ResourceLocation serializerName) {
        super(serializerName, duration);
        this.mainInput = mainInput;
        this.extraInput = extraInput;
        this.output = output;
    }

    public static TwoItemStackToItemStackRecipeBuilder alloySmelting(int duration, ItemStackIngredient mainInput, ItemStackIngredient extraInput, ItemStack output) {
        if (output.isEmpty()) {
            throw new IllegalArgumentException("This alloy smelting recipe requires a non empty item output.");
        } else {
            return new TwoItemStackToItemStackRecipeBuilder(duration, mainInput, extraInput, output, gmSerializer("alloy_smelting"));
        }
    }

    protected TwoItemStackToItemStackRecipeBuilder.@NotNull ItemStackToItemStackRecipeResult getResult(@NotNull ResourceLocation id) {
        return new TwoItemStackToItemStackRecipeBuilder.ItemStackToItemStackRecipeResult(id);
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        this.build(consumer, this.output.getItem());
    }

    public class ItemStackToItemStackRecipeResult extends GMProcessingRecipeBuilder<TwoItemStackToItemStackRecipeBuilder>.RecipeResult {
        protected ItemStackToItemStackRecipeResult(ResourceLocation id) {
            super(id);
        }

        public void serializeRecipeData(@NotNull JsonObject json) {
            json.add("mainInput", TwoItemStackToItemStackRecipeBuilder.this.mainInput.serialize());
            json.add("extraInput", TwoItemStackToItemStackRecipeBuilder.this.extraInput.serialize());
            json.add("itemOutput", SerializerHelper.serializeItemStack(TwoItemStackToItemStackRecipeBuilder.this.output));
        }
    }
}
