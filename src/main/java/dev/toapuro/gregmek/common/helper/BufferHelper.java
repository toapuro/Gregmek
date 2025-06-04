package dev.toapuro.gregmek.common.helper;

import net.minecraft.network.FriendlyByteBuf;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class BufferHelper {
    public static <T> List<T> readList(FriendlyByteBuf byteBuf, IntFunction<T> reader) {
        int length = byteBuf.readVarInt();
        return IntStream.range(0, length)
                .mapToObj(reader)
                .toList();
    }

    public static <T> void writeList(FriendlyByteBuf byteBuf, List<T> tList, Consumer<T> writer) {
        byteBuf.writeVarInt(tList.size());
        for (T t : tList) {
            writer.accept(t);
        }
    }
}
