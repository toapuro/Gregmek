package dev.toapuro.gregmek.core.hooks;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HookContext {
    private final CallbackInfo callbackInfo;
    private boolean cancelled = false;

    public HookContext(CallbackInfo callbackInfo) {
        this.callbackInfo = callbackInfo;
    }

    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void applyMixin() {
        if (cancelled && !callbackInfo.isCancelled()) {
            callbackInfo.cancel();
        }
    }
}
