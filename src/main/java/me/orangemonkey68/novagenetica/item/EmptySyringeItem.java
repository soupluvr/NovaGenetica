package me.orangemonkey68.novagenetica.item;

import me.orangemonkey68.novagenetica.accessor.NovaGeneticaPlayer;
import me.orangemonkey68.novagenetica.helper.item.ItemHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class EmptySyringeItem extends Item {
    public EmptySyringeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient){
            NovaGeneticaPlayer ngPlayer = (NovaGeneticaPlayer) user;
            ItemStack playerSyringe = ItemHelper.getPlayerBackupSyringe((ServerPlayerEntity) user);

            ItemStack stackInHand = user.getStackInHand(hand);
            if(stackInHand.getCount() > 1){
                user.giveItemStack(playerSyringe);
                if(!user.isCreative()){
                    stackInHand.setCount(stackInHand.getCount() - 1);
                    user.setStackInHand(hand, stackInHand);
                }
                return TypedActionResult.success(stackInHand, true);
            } else {
                user.setStackInHand(hand, playerSyringe);
                return TypedActionResult.success(playerSyringe, true);
            }

//            return TypedActionResult.success(playerSyringe, true);

        }

        return TypedActionResult.fail(user.getStackInHand(hand));
    }
}
