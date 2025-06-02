package dev.tdnpgm.gregmek.item;

import dev.tdnpgm.gregmek.tier.GMBaseTier;
import mekanism.api.text.TextComponentUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ProgrammedCircuit extends Item {
    private final GMBaseTier circuitTier;

    public ProgrammedCircuit(Properties prop, GMBaseTier circuitTier) {
        super(prop);
        this.circuitTier = circuitTier;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return TextComponentUtil.build(circuitTier.getColor(), super.getName(stack));
    }
}
