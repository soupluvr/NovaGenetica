package me.orangemonkey68.novagenetica.mixin;

import com.mojang.authlib.GameProfile;
import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaPlayer;
import me.orangemonkey68.novagenetica.abilities.Ability;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements NovaGeneticaPlayer {

    //Note to self: check the class you're mixing into for naming conflicts
    @Unique
    private final Set<Ability> ng_abilities = new HashSet<>();

    @Inject(method = "<init>", at = @At("TAIL"))
    void injectConstructor(MinecraftServer server, ServerWorld world, GameProfile profile, ServerPlayerInteractionManager interactionManager, CallbackInfo ci) {
        
    }

    @Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
    void readAbilitiesFromTag(CompoundTag tag, CallbackInfo ci){
        CompoundTag subTag = (CompoundTag) tag.get("novagenetica_abilities");
        if(subTag == null) return;

        for (int i = 0; subTag.contains(String.valueOf(i)); i++) {
            Ability ability = NovaGenetica.ABILITY_REGISTRY.get(new Identifier(subTag.getString(String.valueOf(i))));
            if(ability != null){
                ng_abilities.add(ability);
            }
        }
    }

    @Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
    void writeAbilitiesToTag(CompoundTag tag, CallbackInfo ci){
        CompoundTag subTag = new CompoundTag();
        List<Ability> ngAbilityList = new ArrayList<>(ng_abilities);
        for (int i = 0; i < ngAbilityList.size(); i++) {
            Identifier id = NovaGenetica.ABILITY_REGISTRY.getId(ngAbilityList.get(i));
            if (id != null) {
                subTag.putString(String.valueOf(i), id.toString());
            }
        }

        tag.put("novagenetica_abilities", subTag);
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
     * @param abilityToCheckId The Identifier of the ability to query
     * @return {@code true} if the player has the ability, {@code false} if otherwise.
     */
    @Override
    public boolean hasAbility(Identifier abilityToCheckId) {
        Ability abilityToCheck = NovaGenetica.ABILITY_REGISTRY.get(abilityToCheckId);
        if(abilityToCheck == null) return false;

        return hasAbility(abilityToCheck);
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
        Ability ability = NovaGenetica.ABILITY_REGISTRY.get(abilityID);

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
        Ability ability = NovaGenetica.ABILITY_REGISTRY.get(abilityID);
        if(ability != null){
            ng_abilities.remove(ability);
        }
    }

    public Set<Ability> getAbilities() {
        return ng_abilities;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    void onTick(CallbackInfo ci){
        //call ability onTick() functions
        ng_abilities.forEach(ability -> {
            if (!ability.isEnabled()) return;
            ability.onTick((ServerPlayerEntity)(Object)this);
        });
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    void onDeath(DamageSource source, CallbackInfo ci){
        ng_abilities.clear();
    }
}
