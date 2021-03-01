package me.orangemonkey68.novagenetica.item;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.abilities.Ability;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CompleteGeneItem extends Item {
    public CompleteGeneItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

//    @Override
//    public String getTranslationKey(ItemStack stack) {
//        CompoundTag tag = stack.getTag();
//        if(tag != null){
//            Identifier id = new Identifier(tag.getString("ability"));
//            if(NovaGenetica.ABILITY_REGISTRY.containsId(id)){
//                return "item.complete_gene." + id.toString();
//            }
//        }
//
//        return "item.gene.unknown";
//    }

    @Override
    public Text getName(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if(tag != null){
            Identifier id = new Identifier(tag.getString("ability"));
            Ability ability = NovaGenetica.ABILITY_REGISTRY.get(id);
            if(ability != null){
                return new TranslatableText(getTranslationKey(), new TranslatableText(ability.getTranslationKey()));
            }
        }

        return new TranslatableText("item.novagenetica.complete_gene.unknown");
    }
}
