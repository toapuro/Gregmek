package dev.toapuro.gregmek.content.registry;

import dev.toapuro.gregmek.Gregmek;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class GMTags {

    public static class Items {
        public static final TagKey<Item> PROGRAMMED_CIRCUIT;

        static {
            PROGRAMMED_CIRCUIT = tag("programmable_circuit");
        }

        @SuppressWarnings("removal")
        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(Gregmek.rl(name));
        }
    }
}
