package me.orangemonkey68.novagenetica.blockentity;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.gui.GeneExtractorGuiDescription;
import me.orangemonkey68.novagenetica.item.MobFlakesItem;
import me.orangemonkey68.novagenetica.item.helper.ItemHelper;
import net.minecraft.command.argument.ItemSlotArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class GeneExtractorBlockEntity extends BaseMachineBlockEntity {
    protected DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public GeneExtractorBlockEntity() {
        super(NovaGenetica.GENE_EXTRACTOR_BLOCK_ENTITY, "block.novagenetica.gene_extractor", NovaGenetica.GENE_EXTRACTOR_ID);
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new GeneExtractorGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void tick() {
        int powerStep = getPowerDrawPerTick();
        int maxProgress = getProcessingTime();

        if(isInputValid() && storedPower >= powerStep && progress <= maxProgress && (itemStacks.get(1) == ItemStack.EMPTY || itemStacks.get(1).getItem() == Items.AIR)){
            progress++;
            storedPower -= powerStep;
        } else {
            progress = 0;
        }

        if(progress >= maxProgress){
            progress = 0;

            ItemStack inputStack = itemStacks.get(0);
            if(inputStack.getCount() > 1){
                inputStack.decrement(1);
            } else {
                itemStacks.set(0, ItemStack.EMPTY);
            }

            itemStacks.set(1, getOutput(inputStack));
            markDirty();
        }
    }

    boolean isInputValid(){
        ItemStack input = itemStacks.get(0);
        CompoundTag tag = input.getTag();

        if(tag != null && input.getItem() instanceof MobFlakesItem){
            //Return true if tag contains the ID of an existing EntityType
            return tag.contains("entityType") && Registry.ENTITY_TYPE.containsId(new Identifier(tag.getString("entityType")));
        }

        return false;
    }

    ItemStack getOutput(ItemStack input){
        CompoundTag tag = input.getTag();
        if(tag.contains("entityType")){
            return ItemHelper.getGene(new Identifier(tag.getString("entityType")), null, false, false);
        }
        //this should literally never run
        return ItemStack.EMPTY;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return itemStacks;
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
        return slot != 1;
    }
}
