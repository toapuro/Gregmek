package dev.toapuro.gregmek.core.hooks;

public interface IMixinHook {
    default int getPriority() {
        return 1000;
    }

    default boolean isEnabled() {
        return true;
    }

    MixinCompatibleSide getSide();

    String getHookId();
}
