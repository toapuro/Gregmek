package dev.toapuro.gregmek.mixin;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.toapuro.gregmek.content.recipe.additional.IHasEnergyRequired;
import mekanism.api.SerializerHelper;
import mekanism.api.math.FloatingLong;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = RecipeManager.class, remap = false)
public abstract class RecipeManagerMixin {
    @ModifyReturnValue(method = "fromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;Lnet/minecraftforge/common/crafting/conditions/ICondition$IContext;)Lnet/minecraft/world/item/crafting/Recipe;",
            at = @At("RETURN"))
    private static Recipe<?> fromJson(Recipe<?> original, @Local(argsOnly = true) JsonObject json) {
        if (original instanceof IHasEnergyRequired energyRequiredHolder) {
            if (json.has("extraEnergyRequired")) {
                FloatingLong energyRequired = SerializerHelper.getFloatingLong(json, "extraEnergyRequired");
                energyRequiredHolder.gregmek$setEnergyRequired(energyRequired);
            } else {
                energyRequiredHolder.gregmek$setEnergyRequired(FloatingLong.ZERO); // FIX: maybe not needed
            }
        }
        return original;
    }

}
