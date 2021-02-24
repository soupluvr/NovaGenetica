package me.orangemonkey68.novageneticae.mixin;

import com.mojang.authlib.GameProfile;
import me.orangemonkey68.novageneticae.NovaGeneticae;
import me.orangemonkey68.novageneticae.NovaGeneticaePlayer;
import me.orangemonkey68.novageneticae.abilities.Ability;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public class PlayerMixin implements NovaGeneticaePlayer {

    //Note to self: check the class you're mixing into for naming conflicts
    private final Set<Ability> ng_abilities = new HashSet<>();

    @Inject(method = "<init>", at = @At("TAIL"))
    void injectConstructor(MinecraftServer server, ServerWorld world, GameProfile profile, ServerPlayerInteractionManager interactionManager, CallbackInfo ci) {
        
    }

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
        NovaGeneticae.LOGGER.info(ng_abilities.toString());

        for (Ability ability : ng_abilities) {
            Identifier abilityID2 = NovaGeneticae.ABILITY_REGISTRY.getId(ability);

            NovaGeneticae.LOGGER.info("ID: {}, Target: {}, Equals (==): {}, Equals(.equals): {}", abilityID2.toString(), abilityID.toString(), abilityID == abilityID2, abilityID.equals(abilityID2) );
            if(NovaGeneticae.ABILITY_REGISTRY.getId(ability) == abilityID) return true;
        }

        return false;
    }

    /**
     * @param ability the ability to give.
     */
    @Override
    public void giveAbility(Ability ability) {
        ng_abilities.add(ability);
    }

    /**
     * @param abilityID the Identifier of the ability to remove
     */
    @Override
    public void giveAbility(Identifier abilityID) {
        Ability ability = NovaGeneticae.ABILITY_REGISTRY.get(abilityID);

        NovaGeneticae.LOGGER.info(ability);

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

    public Set<Ability> getAbilities() {
        return ng_abilities;
    }
}
