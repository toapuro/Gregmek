package dev.tdnpgm.gregmek.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.tdnpgm.gregmek.recipes.IHasEnergyRequired;
import mekanism.api.math.FloatingLong;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundUpdateRecipesPacket.class)
public class ClientboundUpdateRecipesPacketMixin {
    @Inject(method = "toNetwork", at = @At("TAIL"))
    private static <RECIPE extends Recipe<?>> void toNetwork(FriendlyByteBuf buffer, RECIPE recipe, CallbackInfo ci) {
        if (recipe instanceof IHasEnergyRequired energyRequiredHolder) {
            FloatingLong energyRequired = energyRequiredHolder.gregmek$getEnergyRequired();
            energyRequired.writeToBuffer(buffer);
        }
    }

    @ModifyReturnValue(method = "fromNetwork", at = @At("RETURN"))
    private static Recipe<?> fromNetwork(Recipe<?> original, @Local(argsOnly = true) FriendlyByteBuf buffer) {
        if (original instanceof IHasEnergyRequired energyRequiredHolder) {
            energyRequiredHolder.gregmek$setEnergyRequired(FloatingLong.readFromBuffer(buffer));
        }
        return original;
    }
}
