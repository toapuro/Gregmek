package dev.toapuro.gregmek.content.registry;

import dev.toapuro.gregmek.Gregmek;
import dev.toapuro.gregmek.content.tile.TileEntityAlloySmelter;
import dev.toapuro.gregmek.content.tile.TileEntityAssembler;
import dev.toapuro.gregmek.content.tile.TileEntityBender;
import dev.toapuro.gregmek.content.tile.container.GMTileContainer;
import dev.toapuro.gregmek.core.mixin.accessor.ContainerTypeRegistryObjectAccessor;
import dev.toapuro.gregmek.core.mixin.accessor.WrappedDeferredRegisterAccessor;
import mekanism.common.inventory.container.type.MekanismContainerType;
import mekanism.common.registration.INamedEntry;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.inventory.MenuType;

import java.util.Objects;
import java.util.function.Supplier;

public class GMContainerTypes {
    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(Gregmek.MODID);
    public static final ContainerTypeRegistryObject<GMTileContainer<TileEntityAssembler>> ASSEMBLER;
    public static final ContainerTypeRegistryObject<GMTileContainer<TileEntityAlloySmelter>> ALLOY_SMELTER;
    public static final ContainerTypeRegistryObject<GMTileContainer<TileEntityBender>> BENDER;

    static {
        ASSEMBLER = register(GMBlocks.ASSEMBLING_MACHINE, TileEntityAssembler.class);
        ALLOY_SMELTER = register(GMBlocks.ALLOY_SMELTER, TileEntityAlloySmelter.class);
        BENDER = register(GMBlocks.BENDER, TileEntityBender.class);
    }

    @SuppressWarnings("unchecked")
    public static <TILE extends TileEntityMekanism> ContainerTypeRegistryObject<GMTileContainer<TILE>> register(String name, Class<TILE> tileClass) {
        ContainerTypeRegistryObject<GMTileContainer<TILE>> registryObject = new ContainerTypeRegistryObject<>(null);
        ContainerTypeRegistryObjectAccessor<GMTileContainer<TILE>> registryAccess = (ContainerTypeRegistryObjectAccessor<GMTileContainer<TILE>>) registryObject;

        MekanismContainerType.IMekanismContainerFactory<TILE, GMTileContainer<TILE>> factory = (id, inv, data) ->
                new GMTileContainer<>(registryObject, id, inv, data);
        Supplier<MenuType<GMTileContainer<TILE>>> supplier = () -> MekanismContainerType.tile(tileClass, factory);
        Objects.requireNonNull(registryObject);
        WrappedDeferredRegisterAccessor<MenuType<?>> containerRegistryAccess = (WrappedDeferredRegisterAccessor<MenuType<?>>) CONTAINER_TYPES;
        return containerRegistryAccess.invokeRegister(name, supplier, registryAccess::invokeSetRegistryObject);
    }

    public static <TILE extends TileEntityMekanism> ContainerTypeRegistryObject<GMTileContainer<TILE>> register(INamedEntry nameProvider, Class<TILE> tileClass) {
        return register(nameProvider.getInternalRegistryName(), tileClass);
    }
}
