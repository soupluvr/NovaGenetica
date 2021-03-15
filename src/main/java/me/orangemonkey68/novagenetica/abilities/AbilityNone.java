package me.orangemonkey68.novagenetica.abilities;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.client.NovaGeneticaClient;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

import java.util.HashSet;
import java.util.Set;

public class AbilityNone implements Ability {
    /**
     * @return The translation key string to pass into {@link #getName()}
     */
    @Override
    public String getTranslationKey() {
        return "ability.novagenetica.none";
    }

    /**
     * This number should <b>always</b> be even. If it isn't, an {@link IllegalStateException} will be logged and it will be removed from the registry.
     *
     * @return The number of genes needed to create one "Completed Gene".
     */
    @Override
    public int genesNeededToComplete() {
        return 0;
    }

    /**
     * It's recommended to check a config here.
     *
     * @return Whether or not the player should be allowed to get this Ability or the items to make it.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * This code is run in {@link NovaGenetica#onInitialize()}, and it's recommended that register any event callbacks and server packet receivers in this function.
     */
    @Override
    public void onRegistryServer() {

    }

    /**
     * This code is run in {@link NovaGeneticaClient#onInitializeClient()}, and it's recommended to register any client packet recievers in this function.
     */
    @Override
    public void onRegistryClient() {

    }

    @Override
    public int getLootTableWeight() {
        return 0;
    }

    @Override
    public Set<EntityType> getEntityTypes() {
        return new HashSet<>();
    }

    /**
     * This code is run when the player injects the ability into themself.
     * <b>NOTE:</b> this will <b>always</b> run on the server.
     *
     * @param player The player who just injected
     */
    @Override
    public void onInjection(ServerPlayerEntity player) {
        player.sendMessage(new TranslatableText("message.novagenetica.ability.none"), false);
    }

    /**
     * This code is executes every tick that a player has an ability.
     *
     * @param player The player the ability is executing on
     */
    @Override
    public void onTick(ServerPlayerEntity player) {

    }

    /**
     * @return the color of this Ability's items in 0xRRGGBB format.
     */
    @Override
    public int getColor() {
        return 0xFFFFFF;
    }
}
