package dev.tdnpgm.gregmek.mixin.accessor;

import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ContainerTypeRegistryObject.class, remap = false)
public interface ContainerTypeRegistryObjectAccessor<CONTAINER extends AbstractContainerMenu> {
    @Invoker("setRegistryObject")
    ContainerTypeRegistryObject<CONTAINER> invokeSetRegistryObject(RegistryObject<MenuType<CONTAINER>> registryObject);
}
