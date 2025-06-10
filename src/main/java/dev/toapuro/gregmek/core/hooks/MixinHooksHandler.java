package dev.toapuro.gregmek.core.hooks;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class MixinHooksHandler {
    private static final Map<String, IMixinHook> mixinHooks = new HashMap<>();

    public static void registerHook(IMixinHook mixinHook) {
        mixinHooks.put(mixinHook.getHookId(), mixinHook);

    }

    public static <T extends IMixinHook> List<T> getHooks(Class<T> type) {
        return getHooks(type, MixinEnvironment.getCurrentEnvironment().getSide());
    }

    public static <T extends IMixinHook> List<T> getHooks(Class<T> type, MixinEnvironment.Side side) {
        return mixinHooks.values().stream()
                .filter(IMixinHook::isEnabled)
                .filter(mixinHook -> mixinHook.getSide().isCompatibleTo(side))
                .filter(type::isInstance)
                .map(type::cast)
                .sorted(Comparator.comparingInt(IMixinHook::getPriority))
                .toList();
    }

    public static <T extends IMixinHook> void execute(Class<T> type, Consumer<T> hookConsumer) {
        getHooks(type, MixinEnvironment.getCurrentEnvironment().getSide()).forEach(hookConsumer);
    }

    public static <T extends IMixinHook> void execute(Class<T> type, MixinEnvironment.Side side, Consumer<T> hookConsumer) {
        getHooks(type, side).forEach(hookConsumer);
    }

    public static <T extends IMixinHook, U> Optional<U> findFirst(Class<T> type, MixinEnvironment.Side side, Function<T, @Nullable U> hookFunction) {
        for (T hook : getHooks(type, side)) {
            U value = hookFunction.apply(hook);
            if (Objects.nonNull(value)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public static <T extends IMixinHook> void untilFalse(Class<T> type, MixinEnvironment.Side side, Function<T, Boolean> hookFunction) {
        for (T hook : getHooks(type, side)) {
            if (!hookFunction.apply(hook)) {
                return;
            }
        }
    }
}
