package dev.tdnpgm.gregmek.content.recipe.additional;

import mekanism.api.math.FloatingLong;

public interface IHasEnergyRequired {
    FloatingLong gregmek$getEnergyRequired();

    void gregmek$setEnergyRequired(FloatingLong energyRequired);
}
