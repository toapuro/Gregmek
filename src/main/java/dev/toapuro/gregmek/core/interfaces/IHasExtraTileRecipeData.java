package dev.toapuro.gregmek.core.interfaces;

import mekanism.api.math.FloatingLong;

public interface IHasExtraTileRecipeData {
    FloatingLong gregmek$getRecipeExtraEnergyRequired();

    void gregmek$setRecipeExtraEnergyRequired(FloatingLong energyRequired);
}
