package me.orangemonkey68.novagenetica.client;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.gui.Generic1x1GuiDescription;
import me.orangemonkey68.novagenetica.gui.Generic1x1Screen;
import me.orangemonkey68.novagenetica.helper.ColorHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class NovaGeneticaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NovaGenetica.ABILITY_REGISTRY.forEach(Ability::onRegistryClient);
        ScreenRegistry.register(NovaGenetica.GENERIC_1X1_SCREEN_HANDLER_TYPE, Generic1x1Screen::new);
    }
}
