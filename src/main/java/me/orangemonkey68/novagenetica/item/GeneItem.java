package me.orangemonkey68.novagenetica.item;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.abilities.Ability;
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
            if(tag.contains("entityType")){
                Identifier id = new Identifier(tag.getString("entityType"));
                if(Registry.ENTITY_TYPE.containsId(id)){
                    EntityType<?> type = Registry.ENTITY_TYPE.get(id);
                    return new TranslatableText("item.novagenetica.gene", new TranslatableText(type.getTranslationKey()));
                }
            }
            if(tag.contains("ability")){
                Identifier id = new Identifier(tag.getString("ability"));
                if(NovaGenetica.ABILITY_REGISTRY.containsId(id)){
                    Ability ability = NovaGenetica.ABILITY_REGISTRY.get(id);
                    return new TranslatableText("item.novagenetica.complete_gene", new TranslatableText(ability.getTranslationKey()));
                }
            }

        }

        return new TranslatableText("item.gene.unknown");

    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if(tag != null){
            if(tag.contains("complete")){
                return tag.getBoolean("complete");
            }
        }
        return false;
    }
}
