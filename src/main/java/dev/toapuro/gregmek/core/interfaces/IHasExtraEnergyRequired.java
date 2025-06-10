package dev.toapuro.gregmek.core.interfaces;

import mekanism.api.math.FloatingLong;

public interface IHasExtraEnergyRequired {
    FloatingLong gregmek$getExtraEnergyRequired();

    void gregmek$setExtraEnergyRequired(FloatingLong energyRequired);
}
