package me.orangemonkey68.novagenetica.mixin;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
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
            return ngPlayer.hasAbility(new Identifier(NovaGenetica.MOD_ID, "scare_creepers"));
        };

        //Raw use FleeEntityGoal because that's what CreeperEntity does
        //noinspection unchecked
        goalSelector.add(3, new FleeEntityGoal(this, ServerPlayerEntity.class, scare_creepers_predicate, 6.0F, 1.0D, 1.2D, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR));
    }
}
