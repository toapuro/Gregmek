package dev.toapuro.gregmek.core.mixin;

import dev.toapuro.gregmek.common.helper.MixinHelper;
import dev.toapuro.gregmek.core.hooks.MixinHooks;
import dev.toapuro.gregmek.core.hooks.hook.IMekanismGuiMixinHook;
import mekanism.client.gui.GuiMekanism;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiMekanismTile.class, remap = false)
public class GuiMekanismTileMixin<TILE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<TILE>> extends GuiMekanism<CONTAINER> {
    protected GuiMekanismTileMixin(CONTAINER container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Inject(method = "addGuiElements", at = @At("TAIL"))
    public void addGuiElements(CallbackInfo ci) {
        MixinHooks.invoke(IMekanismGuiMixinHook.class, hook ->
                hook.addGuiElements(MixinHelper.cast(this), this::addRenderableWidget)
        );
    }
}
