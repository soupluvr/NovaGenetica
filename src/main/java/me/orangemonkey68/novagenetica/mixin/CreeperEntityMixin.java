package me.orangemonkey68.novagenetica.mixin;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.accessor.NovaGeneticaPlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity {


    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    void injectFleeFromPlayerGoal(CallbackInfo ci){
        Predicate<ServerPlayerEntity> scare_creepers_predicate = (entity) -> {
            NovaGeneticaPlayer ngPlayer = (NovaGeneticaPlayer) entity;
            Ability ability = NovaGenetica.ABILITY_REGISTRY.get(new Identifier(NovaGenetica.MOD_ID, "scare_creepers"));
            if(ability != null){
                return ngPlayer.hasAbility(ability) && ability.isEnabled();
            }
            return false;
        };

        //Raw use FleeEntityGoal because that's what CreeperEntity does
        //noinspection unchecked
        goalSelector.add(0, new FleeEntityGoal(this, ServerPlayerEntity.class, scare_creepers_predicate, 10.0F, 1.0D, 1.2D, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR));
    }
}
