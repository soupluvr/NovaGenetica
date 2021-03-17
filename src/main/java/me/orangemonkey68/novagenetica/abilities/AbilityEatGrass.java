package me.orangemonkey68.novagenetica.abilities;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.accessor.NovaGeneticaPlayer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AbilityEatGrass implements Ability, UseBlockCallback {
    @Override
    public String getTranslationKey() {
        return "ability.novagenetica.eat_grass";
    }

    @Override
    public int genesNeededToComplete() {
        return 2;
    }

    @Override
    public boolean isEnabled() {
        return NovaGenetica.getConfig().abilitiesConfig.eat_grass;
    }

    @Override
    public void onRegistryServer() {
        UseBlockCallback.EVENT.register(this);
    }

    @Override
    public void onRegistryClient() {

    }

    @Override
    public int getLootTableWeight() {
        return 90;
    }

    @Override
    public Set<EntityType> getEntityTypes() {
        return new HashSet<>(Arrays.asList(EntityType.SHEEP));
    }

    /**
     * This code is run when the player injects the ability into themself.
     * <b>NOTE:</b> this will <b>always</b> run on the server.
     */
    @Override
    public void onInjection(ServerPlayerEntity player) {
        player.sendMessage(new TranslatableText("message.novagenetica.ability.eat_grass"), false);
    }

    /**
     * This code is executes every tick that a player has an ability.
     *
     * @param player The player the ability is executing on
     */
    @Override
    public void onTick(ServerPlayerEntity player) {

    }

    /**
     * @return the color of this Ability's items in 0xRRGGBB format.
     */
    @Override
    public int getColor() {
        return 0xFFFFFF;
    }

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {

        if(!world.isClient && isEnabled()){
            NovaGeneticaPlayer ngPlayer = (NovaGeneticaPlayer)player;
            BlockPos pos = hitResult.getBlockPos();
            HungerManager hunger = player.getHungerManager();

            if(world.getBlockState(pos).getBlock() == Blocks.GRASS_BLOCK && ngPlayer.hasAbility(this) && hunger.isNotFull()){
                world.setBlockState(pos, Blocks.DIRT.getDefaultState());

                //TODO: get this hunger value from config
                hunger.add(5, 6f);
                world.playSound(
                        null,
                        pos,
                        SoundEvents.BLOCK_GRASS_BREAK,
                        SoundCategory.BLOCKS,
                        1f,
                        1f
                );
            }
        }

        return ActionResult.PASS;
    }
}
