package me.orangemonkey68.novagenetica.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MobFlakesItem extends Item {
    public MobFlakesItem(Settings settings) {
        super(settings);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if(tag != null){
            Identifier id = new Identifier(tag.getString("entityType"));
            if(Registry.ENTITY_TYPE.containsId(id)){
                return "item.mob_flakes." + id.toString();
            }
        }

        return "item.mob_flakes.unknown";
    }
}
