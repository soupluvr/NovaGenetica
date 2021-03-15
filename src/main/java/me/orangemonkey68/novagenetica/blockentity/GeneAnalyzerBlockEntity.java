package me.orangemonkey68.novagenetica.blockentity;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.item.GeneItem;
import me.orangemonkey68.novagenetica.item.MobFlakesItem;
import me.orangemonkey68.novagenetica.item.helper.ItemHelper;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class GeneAnalyzerBlockEntity extends BaseMachineBlockEntity {

    public GeneAnalyzerBlockEntity() {
        //TODO: register this shit
        super(NovaGenetica.GENE_ANALYZER_BLOCK_ENTITY, "block.novagenetica.gene_extractor", NovaGenetica.GENE_ANALYZER_ID, 2);
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
        return slot == 1 && dir == Direction.DOWN ;
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return null;
    }

    @Override
    public void doneProcessing() {
        itemStacks.set(0, ItemStack.EMPTY);
        //TODO: implement LootTables
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if(slot == 1){
            CompoundTag tag = stack.getTag();

            if(tag != null && stack.getItem() instanceof GeneItem){
                //Return true if tag contains the ID of an existing EntityType
                return tag.contains("entityType") && Registry.ENTITY_TYPE.containsId(new Identifier(tag.getString("entityType")));
            }
        }
        return false;
    }
}
