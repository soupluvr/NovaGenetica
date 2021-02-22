package me.orangemonkey68.novageneticae;

import com.mojang.serialization.Lifecycle;
import me.orangemonkey68.novageneticae.abilities.Ability;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NovaGeneticae implements ModInitializer {
    public static final String MOD_ID = "novageneticae";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final RegistryKey<Registry<Ability>> ABILITY_KEY = RegistryKey.ofRegistry(new Identifier(MOD_ID, "ability"));
    public static final Registry<Ability> ABILITY_REGISTRY = new SimpleRegistry<>(ABILITY_KEY, Lifecycle.stable());

    @Override
    public void onInitialize() {
        //Loops over all abilities, and runs their onRegistry() logic
        ABILITY_REGISTRY.forEach(Ability::onRegistry);
    }
}
