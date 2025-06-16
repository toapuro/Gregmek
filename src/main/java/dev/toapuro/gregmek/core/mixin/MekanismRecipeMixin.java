package dev.toapuro.gregmek.core.mixin;

import dev.toapuro.gregmek.content.tier.GMTier;
import dev.toapuro.gregmek.core.interfaces.IHasExtraRecipeData;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.MekanismRecipe;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MekanismRecipe.class)
public class MekanismRecipeMixin implements IHasExtraRecipeData {
    @Unique
    private FloatingLong gregmek$ExtraEnergyRequired;

    @Unique
    private GMTier gregmek$recipeTier;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(ResourceLocation id, CallbackInfo ci) {
        this.gregmek$ExtraEnergyRequired = FloatingLong.ZERO;
        this.gregmek$recipeTier = GMTier.PRIMITIVE;
    }

    @Override
    public FloatingLong gregmek$getExtraEnergyRequired() {
        return gregmek$ExtraEnergyRequired;
    }

    @Override
    public void gregmek$setExtraEnergyRequired(FloatingLong energyRequired) {
        gregmek$ExtraEnergyRequired = energyRequired;
    }

    @Override
    public GMTier gregmek$getRecipeTier() {
        return gregmek$recipeTier;
    }

    @Override
    public void gregmek$setRecipeTier(GMTier tier) {
        gregmek$recipeTier = tier;
    }
}
