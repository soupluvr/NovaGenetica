package me.orangemonkey68.novagenetica.item;

import me.orangemonkey68.novagenetica.NovaGenetica;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class NovaGeneticaItemColorProvider implements ItemColorProvider {

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        CompoundTag tag = stack.getTag();
        int defaultColor = 0xFFFFFF;
        if(tag == null) return defaultColor;
        if(!tag.contains("color")) return defaultColor;
        if(stack.getItem() == NovaGenetica.GENE_ITEM || stack.getItem() == NovaGenetica.COMPLETE_GENE_ITEM){
            return tintIndex == 0 ? -1 : tag.getInt("color");
        }
        return tag.getInt("color");
    }
}
