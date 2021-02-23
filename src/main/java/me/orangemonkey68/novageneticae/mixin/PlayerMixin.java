package me.orangemonkey68.novageneticae.mixin;

import me.orangemonkey68.novageneticae.NovaGeneticae;
import me.orangemonkey68.novageneticae.NovaGeneticaePlayer;
import me.orangemonkey68.novageneticae.abilities.Ability;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public class PlayerMixin implements NovaGeneticaePlayer {
    //Note to self: check the class you're mixing into for naming conflicts
    private List<Ability> ng_abilities = new ArrayList<>();

    /**
     * @param ability The ability to query.
     * @return {@code true} if the player has the ability, {@code false} if otherwise.
     */
    @Override
    public boolean hasAbility(Ability ability) {
        return ng_abilities.contains(ability);
    }

    /**
     * @param abilityID The Identifier of the ability to query
     * @return {@code true} if the player has the ability, {@code false} if otherwise.
     */
    @Override
    public boolean hasAbility(Identifier abilityID) {
        for (Ability ability : ng_abilities) {
            if(NovaGeneticae.ABILITY_REGISTRY.getId(ability) == abilityID) return true;
        }

        return false;
    }

    /**
     * @param ability the ability to give.
     */
    @Override
    public void giveAbility(Ability ability) {
        if(ng_abilities.contains(ability)) return;

        ng_abilities.add(ability);
    }

    /**
     * @param abilityID the Identifier of the ability to remove
     */
    @Override
    public void giveAbility(Identifier abilityID) {
        Ability ability = NovaGeneticae.ABILITY_REGISTRY.get(abilityID);

        if(ability!= null){
            giveAbility(ability);
        }
    }

    /**
     * @param ability the ability to remove.
     */
    @Override
    public void removeAbility(Ability ability) {
        ng_abilities.remove(ability);
    }

    /**
     * @param abilityID the Identifier of the ability to remove
     */
    @Override
    public void removeAbility(Identifier abilityID) {
        Ability ability = NovaGeneticae.ABILITY_REGISTRY.get(abilityID);
        if(ability != null){
            ng_abilities.remove(NovaGeneticae.ABILITY_REGISTRY.get(abilityID));
        }
    }

}
