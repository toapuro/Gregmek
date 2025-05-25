package java.gregmek.common.recipe.builder;

import com.google.gson.JsonObject;
import mekanism.api.datagen.recipe.MekanismRecipeBuilder;
import mekanism.api.math.FloatingLong;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class GMRecipeBuilder<BUILDER extends MekanismRecipeBuilder<BUILDER>> extends MekanismRecipeBuilder<BUILDER> {
    protected boolean hasExtraEnergy;
    protected FloatingLong extraEnergyRequired;

    protected GMRecipeBuilder(ResourceLocation serializerName, boolean hasExtraEnergy, FloatingLong extraEnergyRequired) {
        super(serializerName);
        this.hasExtraEnergy = hasExtraEnergy;
        this.extraEnergyRequired = extraEnergyRequired;
    }

    public GMRecipeBuilder<BUILDER> extraEnergyRequired(FloatingLong extraEnergyRequired) {
        this.hasExtraEnergy = true;
        this.extraEnergyRequired = extraEnergyRequired;
        return this;
    }

    protected abstract class RecipeResult extends MekanismRecipeBuilder<BUILDER>.RecipeResult implements FinishedRecipe {
        public RecipeResult(ResourceLocation id) {
            super(id);
        }

        @Override
        public @NotNull JsonObject serializeRecipe() {
            JsonObject jsonObject = super.serializeRecipe();
            serializeAdditionalRecipeData(jsonObject);
            return jsonObject;
        }

        public void serializeAdditionalRecipeData(@NotNull JsonObject json) {
            if(GMRecipeBuilder.this.hasExtraEnergy) {
                json.addProperty("energyRequired", GMRecipeBuilder.this.extraEnergyRequired);
            }
        }
    }
}
