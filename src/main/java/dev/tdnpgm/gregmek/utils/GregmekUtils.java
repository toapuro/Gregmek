package dev.tdnpgm.gregmek.utils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GregmekUtils {
    public static <T> List<T> generateNList(Supplier<T> supplier, int n) {
        return IntStream.range(0, n)
                .mapToObj(value -> supplier.get())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static <T> List<T> generateNListWithIndex(Function<Integer, T> supplier, int n) {
        return IntStream.range(0, n)
                .mapToObj(supplier::apply)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static <T, U> List<U> collectOfType(Class<U> toClass, List<T> list){
        return list.stream()
                .filter(toClass::isInstance)
                .map(toClass::cast)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static <T, U> Optional<U> safeCast(Class<U> toClass, T t) {
        if (toClass.isInstance(t)) {
            return Optional.of(toClass.cast(t));
        }

        return Optional.empty();
    }
}
