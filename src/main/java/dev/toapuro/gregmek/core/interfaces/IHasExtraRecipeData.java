package dev.toapuro.gregmek.core.interfaces;

import dev.toapuro.gregmek.content.tier.GMTier;
import mekanism.api.math.FloatingLong;

public interface IHasExtraRecipeData {
    FloatingLong gregmek$getExtraEnergyRequired();
    void gregmek$setExtraEnergyRequired(FloatingLong energyRequired);

    GMTier gregmek$getRecipeTier();

    void gregmek$setRecipeTier(GMTier tier);
}
