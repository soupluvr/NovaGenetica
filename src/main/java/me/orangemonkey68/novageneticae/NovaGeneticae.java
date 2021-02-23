package me.orangemonkey68.novageneticae;

import com.mojang.serialization.Lifecycle;
import me.orangemonkey68.novageneticae.abilities.Ability;
import me.orangemonkey68.novageneticae.abilities.AbilityEatGrass;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;

public class NovaGeneticae implements ModInitializer {
    public static final String MOD_ID = "novageneticae";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final RegistryKey<Registry<Ability>> ABILITY_KEY = RegistryKey.ofRegistry(new Identifier(MOD_ID, "ability"));
    public static final Registry<Ability> ABILITY_REGISTRY = new SimpleRegistry<>(ABILITY_KEY, Lifecycle.stable());

    public static final AbilityEatGrass ABILITY_EAT_GRASS = Registry.register(ABILITY_REGISTRY, new Identifier(MOD_ID, "eat_grass"), new AbilityEatGrass());

    @Override
    public void onInitialize() {




        //Loops over all abilities, and runs their onRegistry() logic
        ABILITY_REGISTRY.forEach(ability -> {
            LOGGER.info(ability.equals(ABILITY_EAT_GRASS));

            ability.onRegistryServer();
            //Throw illegal state if breeding state isn't even
            if(ability.getBreedingState() % 2 != 0){
                throw new IllegalStateException(String.format("Ability \"%s\" does not have an even breeding state.", ability.getName().asString()));
            }
        });

        LOGGER.info(ABILITY_REGISTRY.containsId(new Identifier(MOD_ID, "eat_grass")));

        ServerSidePacketRegistry.INSTANCE.register(new Identifier(MOD_ID, "give_ability_packet"), (packetContext, packetByteBuf) -> {
            NovaGeneticaePlayer ngPlayer = (NovaGeneticaePlayer)packetContext.getPlayer();

            LOGGER.info(packetContext.getPlayer() instanceof ClientPlayerEntity);

            String id = packetByteBuf.readString();
            ((NovaGeneticaePlayer)packetContext.getPlayer()).giveAbility(new Identifier(id));
            LOGGER.info("Gave you the ability: {}", id);
            LOGGER.info(((NovaGeneticaePlayer)packetContext.getPlayer()).getAbilities());
        });



    }
}
