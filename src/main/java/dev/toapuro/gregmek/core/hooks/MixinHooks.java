package dev.toapuro.gregmek.core.hooks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class MixinHooks {
    private static final Logger LOGGER = LoggerFactory.getLogger(MixinHooks.class);

    private static final Map<String, IMixinHook> mixinHooks = new HashMap<>();

    public static void registerHook(IMixinHook mixinHook) {
        System.out.printf("Registering hook %s(%s)%n", mixinHook.getHookId(), mixinHook.isEnabled() ? "enabled" : "disabled");
//        LOGGER.info("Registering hook {}({})", mixinHook.getHookId(), mixinHook.isEnabled() ? "enabled" : "disabled");
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
                .sorted(Comparator.comparingInt(IMixinHook::getPriority).reversed())
                .toList();
    }

    public static <T extends IMixinHook> void invoke(Class<T> type, Consumer<T> hookConsumer) {
        getHooks(type, MixinEnvironment.getCurrentEnvironment().getSide()).forEach(hookConsumer);
    }

    public static <T extends IMixinHook> void invoke(Class<T> type, MixinEnvironment.Side side, Consumer<T> hookConsumer) {
        getHooks(type, side).forEach(hookConsumer);
    }

    public static <T extends IMixinHook, U> Optional<U> findFirst(Class<T> type, MixinEnvironment.Side side, Function<T, Optional<U>> hookFunction) {
        for (T hook : getHooks(type, side)) {
            Optional<U> value = hookFunction.apply(hook);
            if (value.isPresent()) {
                return value;
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
