package gregmek.common.recipe.registries;

import dev.tdnpgm.gregmek.Gregmek;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class GMDatapackRegistryProvider extends BaseDatapackRegistryProvider {

    public GMDatapackRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BUILDER, Gregmek.MODID);
    }

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder();
}