package me.orangemonkey68.novagenetica.client;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.gui.Generic1x1Screen;
import me.orangemonkey68.novagenetica.helper.TextureHelper;
import me.orangemonkey68.novagenetica.networking.NetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class NovaGeneticaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TextureHelper.init();
        NetworkHandler.initClient();

        NovaGenetica.ABILITY_REGISTRY.forEach(Ability::onRegistryClient);
        ScreenRegistry.register(NovaGenetica.GENERIC_1X1_SCREEN_HANDLER_TYPE, Generic1x1Screen::new);
    }
}
