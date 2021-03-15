package me.orangemonkey68.novagenetica.blockentity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.item.helper.NBTHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

public abstract class BaseMachineBlockEntity extends BlockEntity implements
        NamedScreenHandlerFactory,
        ImplementedInventory,
        InventoryProvider,
        SidedInventory,
        Tickable,
        PropertyDelegateHolder,
        EnergyStorage {

    private final String translationKey;
    protected final Identifier blockId;

    public static final int MAX_PROGRESS_INDEX = 0;
    public static final int PROGRESS_INDEX = 1;
    public static final int MAX_STORED_POWER = 2;
    public static final int STORED_POWER = 3;

    protected int progress = 0;
    protected double storedPower = 0;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            if(index == 0){
                return getProcessingTime();
            } else if (index == 1){
                return progress;
            } else if (index == 2){
                return getMaximumPower();
            } else if (index == 3){
                return (int) storedPower;
            }

            return -1;
        }

        @Override
        public void set(int index, int value) {
            if(index == 1){
                progress = value;
            } else if (index == 3){
                storedPower = value;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public BaseMachineBlockEntity(BlockEntityType<?> type, String translationKey, Identifier blockId) {
        super(type);
        this.translationKey = translationKey;
        this.blockId = blockId;
    }

    @Override
    public abstract DefaultedList<ItemStack> getItems();
    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return this;
    }

    @Override
    public abstract int[] getAvailableSlots(Direction side);

    @Override
    public abstract boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir);

    @Override
    public abstract boolean canExtract(int slot, ItemStack stack, Direction dir);

    @Override
    public Text getDisplayName() {
        return new TranslatableText(translationKey);
    }

    @Override
    public abstract @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player);

    @Override
    public abstract void tick();

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, getItems());
        tag.putDouble("storedPower", storedPower);
        tag.putInt("progress", progress);

        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, getItems());
        storedPower = NBTHelper.getDoubleOrDefault(tag, "storedPower", 0);
        progress = NBTHelper.getIntOrDefault(tag, "progress", 0);
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    public int getPowerDrawPerTick(){
        return NovaGenetica.getConfig().machineConfig.powerDrawPerTick;
    }

    public int getMaximumPower(){
        return NovaGenetica.getConfig().machineConfig.maxStoredPower;
    }

    public int getProcessingTime(){
        return NovaGenetica.getConfig().machineConfig.powerDrawPerTick;
    }

    @Override
    public double getMaxInput(EnergySide side) {
        return Double.MAX_VALUE;
    }

    @Override
    public double getMaxOutput(EnergySide side) {
        return 0;
    }

    @Override
    public double getMaxStoredPower() {
        return NovaGenetica.getConfig().machineConfig.maxStoredPower;
    }

    @Override
    public EnergyTier getTier() {
        return EnergyTier.MEDIUM;
    }

    @Override
    public double getStored(EnergySide face) {
        return storedPower;
    }

    @Override
    public void setStored(double amount) {
        this.storedPower = amount;
    }
}
