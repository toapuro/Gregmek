package dev.tdnpgm.gregmek.registry;

import dev.tdnpgm.gregmek.Gregmek;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Gregmek.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DebugEvent {
    @SubscribeEvent
    public static void onServerStarting(ServerStartedEvent event) {
        System.out.println("Printing recipes");
        for (Recipe<?> recipe : event.getServer().getRecipeManager().getRecipes()) {
            Gregmek.DEBUG_LOGGER.info("Recipe id: {} class: {}, type: {}, type object: {}, object: {}",
                    recipe.getId(),
                    recipe.getClass(),
                    recipe.getType(),
                    Integer.toHexString(recipe.getType().hashCode()),
                    Integer.toHexString(recipe.hashCode()));
            System.out.println("ID: " + recipe.getId() + ", Type: " + recipe.getType());
        }
    }
}
