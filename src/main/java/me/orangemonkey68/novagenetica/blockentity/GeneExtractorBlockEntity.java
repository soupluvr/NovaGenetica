package me.orangemonkey68.novagenetica.blockentity;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaConfig;
import me.orangemonkey68.novagenetica.gui.GeneExtractorGuiDescription;
import me.orangemonkey68.novagenetica.item.GeneItem;
import me.orangemonkey68.novagenetica.item.helper.ItemHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class GeneExtractorBlockEntity extends BaseMachineBlockEntity {
    public GeneExtractorBlockEntity() {
        super(NovaGenetica.GENE_EXTRACTOR_BLOCK_ENTITY, 2, "block.novagenetica.gene_extractor", NovaGenetica.GENE_EXTRACTOR_ID);
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new GeneExtractorGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void tick() {
        int powerStep = getPowerDrawPerTick();
        int maxProgress = getProcessingTime();
        if(isInputValid() && storedPower >= powerStep && progress <= maxProgress){
            progress++;
            storedPower -= powerStep;
        } else {
            progress = 0;
        }

        //If it's done processing
        if(progress >= maxProgress){
            progress = 0;
            inventory.set(1, ItemHelper.getGene(null, new Identifier(NovaGenetica.MOD_ID, "eat_grass"), true, false));
            inventory.set(0, ItemStack.EMPTY);
        }
    }

    boolean isInputValid(){
        ItemStack input = inventory.get(0);
        CompoundTag tag = input.getTag();
        if(tag != null){
            if(tag.contains("complete") && tag.contains("entityType")){
                return input.getItem() instanceof GeneItem && !tag.getBoolean("complete");
            }
        }
        return false;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if(side == Direction.UP){
            return new int[]{0};
        } else if (side == Direction.DOWN){
            return new int[]{0};
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
