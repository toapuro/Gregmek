package dev.toapuro.gregmek.core.mixin;

import com.google.gson.JsonObject;
import dev.toapuro.gregmek.core.hooks.MixinHooksHandler;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismRecipeMixinHook;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RecipeManager.class, remap = false)
public abstract class RecipeManagerMixin {
    @Inject(method = "fromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;Lnet/minecraftforge/common/crafting/conditions/ICondition$IContext;)Lnet/minecraft/world/item/crafting/Recipe;",
            at = @At("RETURN"))
    private static void fromJson(ResourceLocation resourceLocation, JsonObject json, ICondition.IContext context, CallbackInfoReturnable<Recipe<?>> cir) {
        MixinHooksHandler.getHooks(IMekanismRecipeMixinHook.class).forEach(hook ->
                hook.readFromJson(json, cir.getReturnValue()));
    }
}
