package dev.tdnpgm.gregmek.recipes.additional;

import mekanism.api.math.FloatingLong;

public interface IHasEnergyRequired {
    FloatingLong gregmek$getEnergyRequired();

    void gregmek$setEnergyRequired(FloatingLong energyRequired);
}
