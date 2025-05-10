package dev.tdnpgm.gregmek.predicates;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface AssemblerPredicate<ITEM extends ItemStack, FLUID extends FluidStack> {
    boolean test();
}
