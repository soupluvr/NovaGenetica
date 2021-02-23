package me.orangemonkey68.novageneticae.abilities;

import me.orangemonkey68.novageneticae.NovaGeneticae;
import me.orangemonkey68.novageneticae.NovaGeneticaePlayer;
import me.orangemonkey68.novageneticae.client.NovaGeneticaeClient;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class AbilityEatGrass implements Ability, UseBlockCallback {
    @Override
    public String getTranslationKey() {
        return "ability.novageneticae.eat_grass";
    }

    @Override
    public int getRarity() {
        return 0;
    }

    @Override
    public int getBreedingState() {
        return 2;
    }

    @Override
    public boolean isAllowed() {
        //TODO: Config
        return true;
    }

    @Override
    public void onRegistryServer() {
        UseBlockCallback.EVENT.register(this);
    }

    @Override
    public void onRegistryClient() {

    }

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        NovaGeneticae.LOGGER.info("Function called on side: {}", world.isClient ? "client" : "server");

        if(!world.isClient){
            NovaGeneticae.LOGGER.info("We're on the server!");

            NovaGeneticaePlayer ngPlayer = (NovaGeneticaePlayer)player;
            BlockPos pos = hitResult.getBlockPos();
            HungerManager hunger = player.getHungerManager();

            NovaGeneticae.LOGGER.info("Has ability: {}", ngPlayer.hasAbility(this));
            NovaGeneticae.LOGGER.info("Hunger not full: {}", hunger.isNotFull());

            if(world.getBlockState(pos).getBlock() == Blocks.GRASS_BLOCK && ngPlayer.hasAbility(this) && hunger.isNotFull()){
                world.setBlockState(pos, Blocks.DIRT.getDefaultState());

                NovaGeneticae.LOGGER.info("Hunger and saturation before: {}, {}", hunger.getFoodLevel(), hunger.getSaturationLevel());

                //TODO: get this hunger value from config
                hunger.add(5, 6f);

                NovaGeneticae.LOGGER.info("Hunger and saturation after: {}, {}", hunger.getFoodLevel(), hunger.getSaturationLevel());
            }
        }

        return ActionResult.PASS;
    }
}
