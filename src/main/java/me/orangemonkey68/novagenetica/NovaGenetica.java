package me.orangemonkey68.novagenetica;

import com.mojang.serialization.Lifecycle;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.abilities.AbilityEatGrass;
import me.orangemonkey68.novagenetica.abilities.AbilityValidator;
import me.orangemonkey68.novagenetica.abilities.RegistrationHelper;
import me.orangemonkey68.novagenetica.commands.GiveAbilityCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NovaGenetica implements ModInitializer {
    public static final String MOD_ID = "novagenetica";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final RegistryKey<Registry<Ability>> ABILITY_KEY = RegistryKey.ofRegistry(new Identifier(MOD_ID, "ability"));
    public static final Registry<Ability> ABILITY_REGISTRY = new SimpleRegistry<>(ABILITY_KEY, Lifecycle.stable());

    @Override
    public void onInitialize() {
        RegistrationHelper.register(
                new AbilityEatGrass(),
                new Identifier(MOD_ID, "eat_grass"),
                new Identifier(MOD_ID, "eat_grass_syringe"),
                new HashSet<>(Arrays.asList(EntityType.SHEEP)),
                0xFFFFFF
        );

        //Loops over all abilities, and runs their onRegistry() logic
        ABILITY_REGISTRY.forEach(Ability::onRegistryServer);

        //Register /giveability command
        //TODO: Fix /giveability command
        CommandRegistrationCallback.EVENT.register(new GiveAbilityCommand());
    }
}
