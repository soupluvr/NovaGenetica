package me.orangemonkey68.novagenetica.client;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.gui.GeneExtractorGuiDescription;
import me.orangemonkey68.novagenetica.gui.GeneExtractorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class NovaGeneticaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NovaGenetica.ABILITY_REGISTRY.forEach(Ability::onRegistryClient);
        ScreenRegistry.<GeneExtractorGuiDescription, GeneExtractorScreen>register(NovaGenetica.GENE_EXTRACTOR_SCREEN_HANDLER, (gui, inventory, title) -> new GeneExtractorScreen(gui, inventory.player, title));
    }
}
