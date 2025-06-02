package dev.toapuro.gregmek.content.registry;

import dev.toapuro.gregmek.content.item.ProgrammedCircuit;
import dev.toapuro.gregmek.content.resource.GMResourceType;
import dev.toapuro.gregmek.content.tier.GMBaseTier;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.resource.IResource;
import mekanism.common.resource.MiscResource;
import net.minecraft.world.item.Item;

public class GMItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister("mekanism");
    public static final ItemRegistryObject<Item> STEEL_PLATE;
    public static final ItemRegistryObject<Item> BRONZE_PLATE;
    public static final ItemRegistryObject<Item> DIAMOND_PLATE;
    public static final ItemRegistryObject<Item> NETHERITE_PLATE;
    public static final ItemRegistryObject<Item> OBSIDIAN_PLATE;
    public static final ItemRegistryObject<Item> REFINED_OBSIDIAN_PLATE;
    public static final ItemRegistryObject<Item> REFINED_GLOWSTONE_PLATE;

    public static final ItemRegistryObject<Item> PRIMITIVE_PROGRAMMED_CIRCUIT;
    public static final ItemRegistryObject<Item> CRUDE_PROGRAMMED_CIRCUIT;
    public static final ItemRegistryObject<Item> INDUSTRIAL_PROGRAMMED_CIRCUIT;
    public static final ItemRegistryObject<Item> IMPROVED_PROGRAMMED_CIRCUIT;
    public static final ItemRegistryObject<Item> REFINED_PROGRAMMED_CIRCUIT;
    public static final ItemRegistryObject<Item> INTEGRATED_PROGRAMMED_CIRCUIT;
    public static final ItemRegistryObject<Item> FABRICATED_PROGRAMMED_CIRCUIT;
    public static final ItemRegistryObject<Item> QUANTUM_PROGRAMMED_CIRCUIT;
    public static final ItemRegistryObject<Item> ENTANGLED_PROGRAMMED_CIRCUIT;

    static {
        STEEL_PLATE = registerPlate(MiscResource.STEEL);
        BRONZE_PLATE = registerPlate(MiscResource.BRONZE);
        DIAMOND_PLATE = registerPlate(MiscResource.DIAMOND);
        NETHERITE_PLATE = registerPlate(MiscResource.NETHERITE);
        OBSIDIAN_PLATE = registerPlate(MiscResource.OBSIDIAN);
        REFINED_OBSIDIAN_PLATE = registerPlate(MiscResource.REFINED_OBSIDIAN);
        REFINED_GLOWSTONE_PLATE = registerPlate(MiscResource.REFINED_GLOWSTONE);

        PRIMITIVE_PROGRAMMED_CIRCUIT = registerProgrammedCircuit(GMBaseTier.PRIMITIVE);
        CRUDE_PROGRAMMED_CIRCUIT = registerProgrammedCircuit(GMBaseTier.CRUDE);
        INDUSTRIAL_PROGRAMMED_CIRCUIT = registerProgrammedCircuit(GMBaseTier.INDUSTRIAL);
        IMPROVED_PROGRAMMED_CIRCUIT = registerProgrammedCircuit(GMBaseTier.IMPROVED);
        REFINED_PROGRAMMED_CIRCUIT = registerProgrammedCircuit(GMBaseTier.REFINED);
        INTEGRATED_PROGRAMMED_CIRCUIT = registerProgrammedCircuit(GMBaseTier.INTEGRATED);
        FABRICATED_PROGRAMMED_CIRCUIT = registerProgrammedCircuit(GMBaseTier.FABRICATED);
        QUANTUM_PROGRAMMED_CIRCUIT = registerProgrammedCircuit(GMBaseTier.QUANTUM);
        ENTANGLED_PROGRAMMED_CIRCUIT = registerProgrammedCircuit(GMBaseTier.ENTANGLED);
    }

    private static ItemRegistryObject<Item> registerPlate(IResource resource) {
        String prefix = GMResourceType.PLATE.getRegistryPrefix();
        return ITEMS.register(prefix + "_" + resource.getRegistrySuffix());
    }

    private static ItemRegistryObject<Item> registerProgrammedCircuit(GMBaseTier tier) {
        return ITEMS.register(tier.getLowerName() + "_programmed_circuit", () ->
                new ProgrammedCircuit(new Item.Properties(), tier));
    }
}
