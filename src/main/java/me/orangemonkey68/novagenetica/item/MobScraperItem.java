package me.orangemonkey68.novagenetica.item;

import me.orangemonkey68.novagenetica.NovaGeneticaEntityType;
import me.orangemonkey68.novagenetica.item.helper.ItemHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;

public class MobScraperItem extends Item {
    public MobScraperItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(!user.world.isClient){
            EntityType<?> entityType = entity.getType();
            NovaGeneticaEntityType ngEntityType = (NovaGeneticaEntityType) entityType;
            if(!ngEntityType.getAbilities().isEmpty()){
                ItemStack stackToDrop = ItemHelper.getMobFlakes(Registry.ENTITY_TYPE.getId(entityType));
            }
        }

        return ActionResult.FAIL;
    }
}
