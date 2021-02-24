package me.orangemonkey68.novageneticae;

import me.orangemonkey68.novageneticae.abilities.Ability;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Set;

public interface NovaGeneticaePlayer {
    /**
     *
     * @param ability The ability to query.
     * @return {@code true} if the player has the ability, {@code false} if otherwise.
     */
    boolean hasAbility(Ability ability);

    /**
     *
     * @param abilityID The Identifier of the ability to query
     * @return {@code true} if the player has the ability, {@code false} if otherwise.
     */
    boolean hasAbility(Identifier abilityID);

    /**
     *
     * @param ability the ability to give.
     */
    void giveAbility(Ability ability);

    /**
     *
     * @param abilityID the Identifier of the ability to remove
     */
    void giveAbility(Identifier abilityID);

    /**
     *
     * @param ability the ability to remove.
     */
    void removeAbility(Ability ability);

    /**
     *
     * @param abilityID the Identifier of the ability to remove
     */
    void removeAbility(Identifier abilityID);

    Set<Ability> getAbilities();
}
