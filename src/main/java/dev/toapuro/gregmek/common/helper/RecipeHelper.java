package dev.toapuro.gregmek.common.helper;

import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class RecipeHelper {
    public static int validateDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive.");
        }
        return duration;
    }

    public static ItemStack requireNotEmpty(ItemStack itemStack, RuntimeException nullException, RuntimeException emptyException) {
        if (Objects.isNull(itemStack)) {
            throw nullException;
        }
        if (itemStack.isEmpty()) {
            throw emptyException;
        }
        return itemStack;
    }
}
