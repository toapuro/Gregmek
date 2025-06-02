package dev.tdnpgm.gregmek.content.recipe.jei;

import dev.tdnpgm.gregmek.Gregmek;
import dev.tdnpgm.gregmek.content.registry.GMBlocks;
import dev.tdnpgm.gregmek.content.registry.recipe.GMJEIRecipeTypes;
import dev.tdnpgm.gregmek.content.registry.recipe.GMRecipeType;
import mekanism.client.jei.CatalystRegistryHelper;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.client.jei.RecipeRegistryHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@JeiPlugin
public class GregmekJEI implements IModPlugin {
    private static final Map<MekanismJEIRecipeType<?>, RecipeType<?>> recipeTypeInstanceCache = new HashMap<>();

    public GregmekJEI() {
    }

    public @NotNull ResourceLocation getPluginUid() {
        return Gregmek.rl("jei_plugin");
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registry) {
    }

    @Override
    public void registerIngredients(@NotNull IModIngredientRegistration registry) {
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new AssemblerRecipeCategory(guiHelper, GMJEIRecipeTypes.ASSEMBLING));
        registry.addRecipeCategories(new AlloySmelterRecipeCategory(guiHelper, GMJEIRecipeTypes.ALLOY_SMELTING));
        registry.addRecipeCategories(new BenderRecipeCategory(guiHelper, GMJEIRecipeTypes.BENDING));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registry) {
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registry) {
        RecipeRegistryHelper.register(registry, GMJEIRecipeTypes.ASSEMBLING, GMRecipeType.ASSEMBLING);
        RecipeRegistryHelper.register(registry, GMJEIRecipeTypes.ALLOY_SMELTING, GMRecipeType.ALLOY_SMELTING);
        RecipeRegistryHelper.register(registry, GMJEIRecipeTypes.BENDING, GMRecipeType.BENDING);

    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registry) {
        CatalystRegistryHelper.register(registry, GMBlocks.ASSEMBLING_MACHINE);
        CatalystRegistryHelper.register(registry, GMBlocks.ALLOY_SMELTER);
        CatalystRegistryHelper.register(registry, GMBlocks.BENDER);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {
        IRecipeTransferHandlerHelper transferHelper = registry.getTransferHelper();
        IStackHelper stackHelper = registry.getJeiHelpers().getStackHelper();
    }
}
