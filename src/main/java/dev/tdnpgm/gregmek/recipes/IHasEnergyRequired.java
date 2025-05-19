package dev.tdnpgm.gregmek.recipes;

import mekanism.api.math.FloatingLong;

public interface IHasEnergyRequired {
    FloatingLong gregmek$getEnergyRequired();

    void gregmek$setEnergyRequired(FloatingLong energyRequired);
}
