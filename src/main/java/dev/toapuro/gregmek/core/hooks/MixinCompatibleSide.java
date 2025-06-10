package dev.toapuro.gregmek.core.hooks;

import org.spongepowered.asm.mixin.MixinEnvironment;

public enum MixinCompatibleSide {
    SERVER,
    CLIENT,
    COMMON;

    public boolean onServer() {
        return this == SERVER || this == COMMON;
    }

    public boolean onClient() {
        return this == CLIENT || this == COMMON;
    }

    public boolean isCompatibleTo(MixinEnvironment.Side side) {
        if (side == MixinEnvironment.Side.UNKNOWN) {
            return true;
        }

        return (side == MixinEnvironment.Side.SERVER && onServer()) ||
                (side == MixinEnvironment.Side.CLIENT && onClient());
    }
}
