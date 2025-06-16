package dev.toapuro.gregmek.core.hooks.hook;

import dev.toapuro.gregmek.core.hooks.IMixinHook;
import mekanism.common.inventory.container.SelectedWindowData;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface IMekanismContainerMixinHook extends IMixinHook {
    default <TILE extends TileEntityMekanism> void addSlots(MekanismTileContainer<TILE> container, TILE tile, Consumer<Slot> addSlotConsumer) {
    }

    default <TILE extends TileEntityMekanism> @NotNull ItemStack quickMoveStackBefore(@NotNull ItemStack stack, MekanismTileContainer<TILE> container, TILE tile, SelectedWindowData selectedWindow) {
        return stack;
    }

    default <TILE extends TileEntityMekanism> @NotNull ItemStack quickMoveStackAfter(@NotNull ItemStack stack, MekanismTileContainer<TILE> container, TILE tile, SelectedWindowData selectedWindow) {
        return stack;
    }
}
