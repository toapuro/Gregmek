package dev.tdnpgm.gregmek.mixin;

import dev.tdnpgm.gregmek.content.recipe.additional.IHasEnergyRequired;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.MekanismRecipe;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MekanismRecipe.class)
public class MekanismRecipeMixin implements IHasEnergyRequired {
    @Unique
    private FloatingLong gregmek$energyRequired;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(ResourceLocation id, CallbackInfo ci) {
        this.gregmek$energyRequired = FloatingLong.ZERO;
    }

    @Override
    public FloatingLong gregmek$getEnergyRequired() {
        return gregmek$energyRequired;
    }

    @Override
    public void gregmek$setEnergyRequired(FloatingLong energyRequired) {
        gregmek$energyRequired = energyRequired;
    }
}
