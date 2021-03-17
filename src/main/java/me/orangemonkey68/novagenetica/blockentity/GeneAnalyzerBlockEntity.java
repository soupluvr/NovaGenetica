package me.orangemonkey68.novagenetica.blockentity;

import me.orangemonkey68.novagenetica.helper.registration.LootTableHelper;
import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.gui.Generic1x1GuiDescription;
import me.orangemonkey68.novagenetica.item.GeneItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GeneAnalyzerBlockEntity extends BaseMachineBlockEntity {

    public GeneAnalyzerBlockEntity() {
        //TODO: register this shit
        super(NovaGenetica.GENE_ANALYZER_BLOCK_ENTITY_TYPE, "block.novagenetica.gene_analyzer", NovaGenetica.GENE_ANALYZER_ID, 2);
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
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new Generic1x1GuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void doneProcessing() {
        if(world != null && !world.isClient){
            ItemStack inputStack = itemStacks.get(0);
            CompoundTag tag = inputStack.getTag();
            NovaGenetica.LOGGER.info("AAAAAAAAA");
            if(tag != null){
                if(tag.contains("entityType")){
                    Identifier entityTypeId = new Identifier(tag.getString("entityType"));
                    if(Registry.ENTITY_TYPE.containsId(entityTypeId) && LootTableHelper.LOOT_TABLE_REGISTRY.containsId(entityTypeId)){
                        //get the loot table for this entity
                        LootTable lootTable = LootTableHelper.LOOT_TABLE_REGISTRY.get(entityTypeId);
                        //Generate the context
                        LootContext context = new LootContext.Builder((ServerWorld) world).random(world.random).build(LootContextTypes.EMPTY);
                        //generate the loot. IDK why it's saying it can be null
                        if(lootTable != null){
                            List<ItemStack> loot = lootTable.generateLoot(context);
                            NovaGenetica.LOGGER.info(lootTable.toString());
                            //print out all loot generated. This should be 1 in length
//                            System.out.println(loot.toString());

                            itemStacks.set(0, ItemStack.EMPTY);
                            itemStacks.set(1, loot.get(0));
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if(slot == 0){
            CompoundTag tag = stack.getTag();

            if(tag != null && stack.getItem() instanceof GeneItem){
                Identifier entityId = new Identifier(tag.getString("entityType"));
                //Return true if tag contains the ID of an existing EntityType
                return tag.contains("entityType") &&
                        Registry.ENTITY_TYPE.containsId(entityId) &&
                        LootTableHelper.LOOT_TABLE_REGISTRY.containsId(entityId);
            }
        } else if (slot == 1){
            return (itemStacks.get(1) == ItemStack.EMPTY || itemStacks.get(1).getItem() == Items.AIR);
        }
        return false;
    }
}
