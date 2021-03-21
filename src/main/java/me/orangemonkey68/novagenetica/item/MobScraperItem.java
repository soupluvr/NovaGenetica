package me.orangemonkey68.novagenetica.item;

import me.orangemonkey68.novagenetica.networking.NetworkHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class MobScraperItem extends ToolItem {

    public MobScraperItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }



    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(!user.world.isClient()){
            if(!entity.isInvulnerableTo(DamageSource.player(user))){
                NetworkHandler.dropMobFlakes(entity.getEntityId(), (ServerPlayerEntity) user, hand);

                user.getStackInHand(hand).damage(1, user, playerEntity -> playerEntity.sendToolBreakStatus(hand));

                return ActionResult.success(true);
            }
        }
        return ActionResult.PASS;
    }
}
