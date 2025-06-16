package gregmek.common.recipe.builder.abstracts;

import com.google.gson.JsonObject;
import dev.toapuro.gregmek.content.tier.GMTier;
import mekanism.api.math.FloatingLong;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class GMProcessingRecipeBuilder<BUILDER extends GMRecipeBuilder<BUILDER>> extends GMRecipeBuilder<BUILDER> {
    protected int duration;
    protected GMTier recipeTier;

    protected GMProcessingRecipeBuilder(ResourceLocation serializerName, boolean hasExtraEnergy, FloatingLong extraEnergyRequired, int duration, GMTier recipeTier) {
        super(serializerName, hasExtraEnergy, extraEnergyRequired);
        this.duration = duration;
        this.recipeTier = recipeTier;
    }

    protected GMProcessingRecipeBuilder(ResourceLocation serializerName, int duration) {
        this(serializerName, false, FloatingLong.ZERO, duration, GMTier.PRIMITIVE);
    }

    public GMProcessingRecipeBuilder<BUILDER> duration(int duration) {
        this.duration = duration;
        return this;
    }

    public GMProcessingRecipeBuilder<BUILDER> duration(GMTier recipeTier) {
        this.recipeTier = recipeTier;
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
