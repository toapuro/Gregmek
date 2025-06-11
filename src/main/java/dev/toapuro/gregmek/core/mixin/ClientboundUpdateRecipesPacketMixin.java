package dev.toapuro.gregmek.core.mixin;

import dev.toapuro.gregmek.core.hooks.MixinHooksHandler;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismRecipeMixinHook;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientboundUpdateRecipesPacket.class)
public class ClientboundUpdateRecipesPacketMixin {
    @Inject(method = "toNetwork", at = @At("TAIL"))
    private static <RECIPE extends Recipe<?>> void toNetwork(FriendlyByteBuf buffer, RECIPE recipe, CallbackInfo ci) {
        MixinHooksHandler.execute(IMekanismRecipeMixinHook.class, MixinEnvironment.Side.CLIENT, hook ->
                hook.writeBuffer(buffer, recipe)
        );
    }

    @Inject(method = "fromNetwork", at = @At("RETURN"))
    private static void fromNetwork(FriendlyByteBuf buffer, CallbackInfoReturnable<Recipe<?>> cir) {
        MixinHooksHandler.execute(IMekanismRecipeMixinHook.class, MixinEnvironment.Side.CLIENT, hook ->
                hook.readBuffer(buffer, cir.getReturnValue())
        );
    }
}
