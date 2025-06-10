package dev.toapuro.gregmek.core.hooks;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

public class HookReturnableContext<T> extends HookContext {
    private final CallbackInfoReturnable<T> callbackInfo;
    private T returnValue;

    public HookReturnableContext(CallbackInfoReturnable<T> callbackInfo) {
        super(callbackInfo);
        this.callbackInfo = callbackInfo;
    }

    public void setReturnValue(T value) {
        this.returnValue = value;
    }

    public void applyMixin() {
        super.applyMixin();
        if (Objects.nonNull(returnValue) && !callbackInfo.isCancelled()) {
            callbackInfo.setReturnValue(returnValue);
        }
    }
}