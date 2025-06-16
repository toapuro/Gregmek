package dev.toapuro.gregmek.core.feature;

import dev.toapuro.gregmek.client.gui.element.tab.GuiCircuitConfigTab;
import dev.toapuro.gregmek.common.helper.MixinHelper;
import dev.toapuro.gregmek.content.tile.interfaces.ITileProgrammable;
import dev.toapuro.gregmek.core.hooks.MixinCompatibleSide;
import dev.toapuro.gregmek.core.hooks.MixinHooks;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismContainerMixinHook;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismGuiMixinHook;
import dev.toapuro.gregmek.core.interfaces.IHasCircuitSlot;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiElement;
import mekanism.common.inventory.container.SelectedWindowData;
import mekanism.common.inventory.container.slot.VirtualInventoryContainerSlot;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.function.Consumer;

public class CircuitSlotFeature implements IMekanismContainerMixinHook, IMekanismGuiMixinHook {
    private static final Logger LOGGER = LoggerFactory.getLogger(CircuitSlotFeature.class);

    static {
        MixinHooks.registerHook(new CircuitSlotFeature());
    }

    private CircuitSlotFeature() {
    }

    @Override
    public String getHookId() {
        return "circuit_slot";
    }

    @Override
    public MixinCompatibleSide getSide() {
        return MixinCompatibleSide.COMMON;
    }

    @Override
    public <TILE extends TileEntityMekanism> void addSlots(MekanismTileContainer<TILE> container, TILE tile, Consumer<Slot> addSlotConsumer) {
        LOGGER.info("addSlots {}", tile.getClass());
        if (tile instanceof ITileProgrammable programmable) {
            VirtualInventoryContainerSlot containerSlot = programmable.gregmek$getCircuitComponent().getCircuitSlot().createContainerSlot();

            ((IHasCircuitSlot) container).gregmek$setCircuitSlot(containerSlot);
            addSlotConsumer.accept(containerSlot);
        }
    }

    @Override
    public @NotNull <TILE extends TileEntityMekanism> ItemStack quickMoveStackBefore(@NotNull ItemStack stack, MekanismTileContainer<TILE> container, TILE tile, SelectedWindowData selectedWindow) {
        VirtualInventoryContainerSlot circuitSlot = ((IHasCircuitSlot) container).gregmek$getCircuitSlot();

        return MekanismTileContainer.insertItem(Collections.singletonList(circuitSlot), stack, true, selectedWindow);
    }

    @Override
    public void addGuiElements(GuiMekanismTile<?, ?> guiTile, Consumer<GuiElement> addElementConsumer) {
        TileEntityMekanism tile = guiTile.getTileEntity();
        LOGGER.info("addGuiElements {}", tile.getClass());
        if (tile instanceof ITileProgrammable programmable) {
            addElementConsumer.accept(new GuiCircuitConfigTab<>(guiTile, MixinHelper.cast(tile)));
        }
    }
}
