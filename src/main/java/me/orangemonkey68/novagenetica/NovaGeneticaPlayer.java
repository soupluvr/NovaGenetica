package me.orangemonkey68.novagenetica;

import me.orangemonkey68.novagenetica.abilities.Ability;
import net.minecraft.util.Identifier;

import java.util.Set;

public interface NovaGeneticaPlayer {
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
