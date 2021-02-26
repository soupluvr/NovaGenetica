package me.orangemonkey68.novagenetica.mixin;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaEntityType;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.abilities.RegistrationHelper;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class EntityTypeMixin implements NovaGeneticaEntityType {
    Set<Ability> ng_abilities = new HashSet<>();

    @Override
    public Set<Ability> getAbilities() {
        return ng_abilities;
    }

    /**
     * @param ability the ability to query
     * @return whether this EntityType can produce drops that produce the given ability
     */
    @Override
    public boolean canGiveAbility(Ability ability) {
        return ng_abilities.contains(ability);
    }

    @Override
    public boolean canGiveAbility(Identifier id) {
        Ability ability = NovaGenetica.ABILITY_REGISTRY.get(id);
        if(ability != null){
            return ng_abilities.contains(ability);
        }
        return false;
    }

    /**
     * <b>NOTE:</b> do not try to register abilities after initialization, the genes will never drop. This method is intended for use in the {@link RegistrationHelper} class
     *
     * @param ability the ability to register
     */
    @Override
    public void registerAbility(Ability ability) {
        ng_abilities.add(ability);
    }
}
