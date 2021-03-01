package me.orangemonkey68.novagenetica.item;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GeneItem extends Item {
    public GeneItem(Settings settings) {
        super(settings);
    }

    //TODO: refactor other Items to use this system to get name
    @Override
    public Text getName(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if(tag != null){
            Identifier id = new Identifier(tag.getString("entityType"));
            if(Registry.ENTITY_TYPE.containsId(id)){
                EntityType<?> type = Registry.ENTITY_TYPE.get(id);
                return new TranslatableText("item.novagenetica.gene", new TranslatableText(type.getTranslationKey()));
            }
        }

        return new TranslatableText("item.gene.unknown");

    }
}
