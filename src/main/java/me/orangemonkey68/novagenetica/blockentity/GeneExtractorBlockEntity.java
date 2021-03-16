package me.orangemonkey68.novagenetica.blockentity;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.gui.Generic1x1GuiDescription;
import me.orangemonkey68.novagenetica.item.MobFlakesItem;
import me.orangemonkey68.novagenetica.item.helper.ItemHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class GeneExtractorBlockEntity extends BaseMachineBlockEntity {

    public GeneExtractorBlockEntity() {
        super(NovaGenetica.GENE_EXTRACTOR_BLOCK_ENTITY_TYPE, "block.novagenetica.gene_extractor", NovaGenetica.GENE_EXTRACTOR_ID, 2);
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new Generic1x1GuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void doneProcessing() {
        ItemStack inputStack = itemStacks.get(0);
        if(inputStack.getCount() > 1){
            inputStack.decrement(1);
        } else {
            itemStacks.set(0, ItemStack.EMPTY);
        }

        itemStacks.set(1, getOutput(inputStack));
        markDirty();
    }

    ItemStack getOutput(ItemStack input){
        CompoundTag tag = input.getTag();
        if(tag != null && tag.contains("entityType")){
            return ItemHelper.getGene(new Identifier(tag.getString("entityType")), null, false, false);
        }
        //this should literally never run
        return ItemStack.EMPTY;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if(side == Direction.UP){
            return new int[]{0};
        } else if (side == Direction.DOWN){
            return new int[]{1};
        } else {
            return new int[0];
        }
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 1 && dir == Direction.DOWN;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if(slot == 0){
            CompoundTag tag = stack.getTag();

            if(tag != null && stack.getItem() instanceof MobFlakesItem){
                //Return true if tag contains the ID of an existing EntityType
                return tag.contains("entityType") && Registry.ENTITY_TYPE.containsId(new Identifier(tag.getString("entityType")));
            }
        } else if (slot == 1){
            return (itemStacks.get(1) == ItemStack.EMPTY || itemStacks.get(1).getItem() == Items.AIR);
        }
        return false;
    }
}
