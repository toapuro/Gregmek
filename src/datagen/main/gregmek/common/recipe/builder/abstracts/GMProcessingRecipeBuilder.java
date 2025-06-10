package gregmek.common.recipe.builder.abstracts;

import com.google.gson.JsonObject;
import mekanism.api.math.FloatingLong;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class GMProcessingRecipeBuilder<BUILDER extends GMRecipeBuilder<BUILDER>> extends GMRecipeBuilder<BUILDER> {
    protected int duration;
    protected boolean hasExtraEnergy;

    protected GMProcessingRecipeBuilder(ResourceLocation serializerName, boolean hasExtraEnergy, FloatingLong extraEnergyRequired, int duration) {
        super(serializerName, hasExtraEnergy, extraEnergyRequired);
        this.hasExtraEnergy = hasExtraEnergy;
        this.duration = duration;
    }

    protected GMProcessingRecipeBuilder(ResourceLocation serializerName, int duration) {
        this(serializerName, false, FloatingLong.ZERO, duration);
    }

    public GMProcessingRecipeBuilder<BUILDER> duration(int duration) {
        this.duration = duration;
        return this;
    }

    protected abstract class RecipeResult extends GMRecipeBuilder<BUILDER>.RecipeResult implements FinishedRecipe {
        public RecipeResult(ResourceLocation id) {
            super(id);
        }

        @OverridingMethodsMustInvokeSuper
        public void serializeAdditionalRecipeData(@NotNull JsonObject json) {
            super.serializeAdditionalRecipeData(json);
            json.addProperty("duration", GMProcessingRecipeBuilder.this.duration);
        }
    }
}
