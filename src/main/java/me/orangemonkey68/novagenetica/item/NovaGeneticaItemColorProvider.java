package me.orangemonkey68.novagenetica.item;

import me.orangemonkey68.novagenetica.NovaGenetica;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class NovaGeneticaItemColorProvider implements ItemColorProvider {
    private final int defaultColor;

    public NovaGeneticaItemColorProvider (Item item){
        if(item == NovaGenetica.FILLED_SYRINGE_ITEM) {
            defaultColor = 0xFFFFFF;
        } else if (item == NovaGenetica.GENE_ITEM) {
            defaultColor = 0x109acc;
        } else if (item == NovaGenetica.MOB_FLAKES) {
            defaultColor = 0xdbac1f;
        } else {
            defaultColor = 0xFFFFFF;
        }
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        CompoundTag tag = stack.getTag();
        if(tag == null) return defaultColor;
        if(!tag.contains("color")) return defaultColor;
        return tag.getInt("color");
    }
}
