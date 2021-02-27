package me.orangemonkey68.novagenetica.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class ItemHelper {
    private ItemHelper(){

    }

    @NotNull
    public static ItemStack stackOf(Identifier abilityId, int color, Item item){
        ItemStack stack = new ItemStack(item);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("ability", abilityId.toString());
        tag.putInt("color", color);
        stack.setTag(tag);
        return stack;
    }
}
