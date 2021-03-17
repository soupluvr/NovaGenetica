package me.orangemonkey68.novagenetica.item;

import me.orangemonkey68.novagenetica.accessor.NovaGeneticaEntityType;
import me.orangemonkey68.novagenetica.helper.item.ItemHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

public class MobScraperItem extends ToolItem {

    public MobScraperItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(!user.world.isClient){
            EntityType<?> entityType = entity.getType();
            NovaGeneticaEntityType ngEntityType = (NovaGeneticaEntityType) entityType;
            if(!ngEntityType.getAbilities().isEmpty()){
                ItemStack stackToDrop = ItemHelper.getMobFlakes(Registry.ENTITY_TYPE.getId(entityType));
                Vec3d entityPos = entity.getPos();
                entity.dropStack(stackToDrop);
                entity.damage(DamageSource.player(user), 1);
                user.getStackInHand(hand).damage(1, user, playerEntity -> playerEntity.sendToolBreakStatus(hand));

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.FAIL;
    }
}
