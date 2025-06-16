package dev.toapuro.gregmek.common.helper;

public class MixinHelper {
    @SuppressWarnings("unchecked")
    public static <R, T> R cast(T t) {
        return (R) t;
    }

    @SuppressWarnings("unchecked")
    public static <T, R> R cast(Class<R> rClass, T t) {
        return (R) t;
    }
}
