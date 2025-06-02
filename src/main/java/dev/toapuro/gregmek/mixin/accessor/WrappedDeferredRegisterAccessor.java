package dev.toapuro.gregmek.mixin.accessor;

import mekanism.common.registration.WrappedDeferredRegister;
import mekanism.common.registration.WrappedRegistryObject;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(value = WrappedDeferredRegister.class, remap = false)
public interface WrappedDeferredRegisterAccessor<T> {
    @Invoker("register")
    <I extends T, W extends WrappedRegistryObject<I>> W invokeRegister(String name, Supplier<? extends I> sup, Function<RegistryObject<I>, W> objectWrapper);
}
