package me.orangemonkey68.novagenetica.abilities;

import com.google.common.collect.ImmutableSet;
import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.client.NovaGeneticaClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Ability {
    Map<Ability, Set<EntityType<?>>> ABILITY_ENTITY_MAP = new HashMap<>();

    /**
     *
     * @return The translation key string to pass into {@link #getName()}
     */
    String getTranslationKey();

    /**
     *
     * @return The {@link TranslatableText} which is the text used to show in game
     */
    default TranslatableText getName() {
        return new TranslatableText(getTranslationKey());
    }

    /**
     * The rarity is used to determine how hard it is to get an ability. The higher this value, the lower the chance is to get the ability.
     * Calculated with {@code 1/getRarity()}, unless {@code getRarity() == 0}, in which case it will never drop.
     * @return The rarity score
     */
    int getRarity();

    /**
     *
     * This number should <b>always</b> be even. If it isn't, an {@link IllegalStateException} will be logged and it will be removed from the registry.
     * @return The number of genes needed to create one "Completed Gene".
     */
    int genesNeededToComplete();

    /**
     * It's recommended to check a config here.
     * @return Whether or not the player should be allowed to get this Ability or the items to make it.
     */
    boolean isEnabled();

    /**
     * This code is run in {@link NovaGenetica#onInitialize()}, and it's recommended that register any event callbacks and server packet receivers in this function.
     */
    void onRegistryServer();

    /**
     * This code is run in {@link NovaGeneticaClient#onInitializeClient()}, and it's recommended to register any client packet recievers in this function.
     */
    void onRegistryClient();

    /**
     * This code is run when the player injects the ability into themself.
     * <b>NOTE:</b> this will <b>always</b> run on the server.
     */
    void onInjection(ServerPlayerEntity player);

    /**
     * This code is executes every tick that a player has an ability.
     * @param player The player the ability is executing on
     */
    void onTick(ServerPlayerEntity player);

    /**
     *
     * @return the color of this Ability's items in 0xRRGGBB format.
     */
    int getColor();

    /**
     *
     * @return a set of all entities that can drop items that produce this ability.
     */
    static Set<EntityType<?>> getEntityTypes(Ability ability){
        return ABILITY_ENTITY_MAP.get(ability);
    }
}
