package dev.toapuro.gregmek.core.mixin;

import dev.toapuro.gregmek.core.interfaces.IHasExtraEnergyRequired;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.MekanismRecipe;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MekanismRecipe.class)
public class MekanismRecipeMixin implements IHasExtraEnergyRequired {
    @Unique
    private FloatingLong gregmek$energyRequired;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(ResourceLocation id, CallbackInfo ci) {
        this.gregmek$energyRequired = FloatingLong.ZERO;
    }

    @Override
    public FloatingLong gregmek$getExtraEnergyRequired() {
        return gregmek$energyRequired;
    }

    @Override
    public void gregmek$setExtraEnergyRequired(FloatingLong energyRequired) {
        gregmek$energyRequired = energyRequired;
    }
}
