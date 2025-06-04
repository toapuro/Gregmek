package dev.toapuro.gregmek.content.item;

import dev.toapuro.gregmek.content.tier.GMTier;
import mekanism.api.text.TextComponentUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ProgrammedCircuit extends Item {
    private final GMTier circuitTier;

    public ProgrammedCircuit(Properties prop, GMTier circuitTier) {
        super(prop);
        this.circuitTier = circuitTier;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return TextComponentUtil.build(circuitTier.getColor(), super.getName(stack));
    }
}
