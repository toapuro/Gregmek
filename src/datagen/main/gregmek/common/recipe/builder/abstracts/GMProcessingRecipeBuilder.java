package gregmek.common.recipe.builder.abstracts;

import com.google.gson.JsonObject;
import mekanism.api.math.FloatingLong;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class GMProcessingRecipeBuilder<BUILDER extends GMRecipeBuilder<BUILDER>> extends GMRecipeBuilder<BUILDER> {
    protected int duration;
    protected boolean hasExtraEnergy;
    protected FloatingLong extraEnergyRequired;

    protected GMProcessingRecipeBuilder(ResourceLocation serializerName, boolean hasExtraEnergy, FloatingLong extraEnergyRequired, int duration) {
        super(serializerName, hasExtraEnergy, extraEnergyRequired);
        this.hasExtraEnergy = hasExtraEnergy;
        this.extraEnergyRequired = extraEnergyRequired;
        this.duration = duration;
    }

    public GMProcessingRecipeBuilder<BUILDER> extraEnergyRequired(FloatingLong extraEnergyRequired) {
        this.hasExtraEnergy = true;
        this.extraEnergyRequired = extraEnergyRequired;
        return this;
    }

    public GMProcessingRecipeBuilder<BUILDER> setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    protected abstract class RecipeResult extends GMRecipeBuilder<BUILDER>.RecipeResult implements FinishedRecipe {
        public RecipeResult(ResourceLocation id) {
            super(id);
        }

        public void serializeAdditionalRecipeData(@NotNull JsonObject json) {
            json.addProperty("duration", GMProcessingRecipeBuilder.this.duration);
        }
    }
}
