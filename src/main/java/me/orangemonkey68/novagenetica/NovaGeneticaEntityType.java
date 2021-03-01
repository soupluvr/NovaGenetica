package me.orangemonkey68.novagenetica;

import me.orangemonkey68.novagenetica.abilities.Ability;
import net.minecraft.util.Identifier;

import java.util.Set;

public interface NovaGeneticaEntityType {
    Set<Ability> getAbilities();

    /**
     *
     * @return whether this EntityType can produce drops that produce the given ability
     */
    boolean canGiveAbility(Ability ability);

    boolean canGiveAbility(Identifier id);

    /**
     * <b>NOTE:</b> do not try to register abilities after initialization, the genes will never drop. This method is intended for use in the {@link RegistrationHelper} class
     *
     */
    void registerAbility(Ability ability);

}
