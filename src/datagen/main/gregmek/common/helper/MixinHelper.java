package gregmek.common.helper;

public class MixinHelper {
    @SuppressWarnings("unchecked")
    @Deprecated
    public static <T, R> R cast(T t) {
        return (R) t;
    }

    @SuppressWarnings("unchecked")
    public static <T, R> R cast(T t, Class<R> rClass) {
        return (R) t;
    }
}