package me.orangemonkey68.novagenetica.blockentity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaConfig;
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

public abstract class BaseMachineBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory, InventoryProvider, SidedInventory, Tickable, PropertyDelegateHolder {
    public final DefaultedList<ItemStack> inventory;
    private final String translationKey;
    protected final Identifier blockId;

    public static final int MAX_PROGRESS_INDEX = 0;
    public static final int PROGRESS_INDEX = 1;
    public static final int MAX_STORED_POWER = 2;
    public static final int STORED_POWER = 3;

    protected int progress = 0;
    protected int storedPower = 0;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            if(index == 0){
                NovaGeneticaConfig.PoweredMachineConfig machineConfig = NovaGenetica.getMachineConfigMap().get(blockId);
                return machineConfig != null ? machineConfig.processingTime : 200;
            } else if (index == 1){
                return progress;
            } else if (index == 2){
                NovaGeneticaConfig.PoweredMachineConfig machineConfig = NovaGenetica.getMachineConfigMap().get(blockId);
                return machineConfig != null ? machineConfig.maxStoredPower : 50000;
            } else if (index == 3){
                return storedPower;
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

    public BaseMachineBlockEntity(BlockEntityType<?> type, int inventorySlots, String translationKey, Identifier blockId) {
        super(type);
        this.inventory = DefaultedList.ofSize(inventorySlots, ItemStack.EMPTY);
        this.translationKey = translationKey;
        this.blockId = blockId;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

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
        Inventories.toTag(tag, inventory);
        tag.putInt("storedPower", storedPower);
        tag.putInt("progress", progress);

        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, inventory);
        storedPower = NBTHelper.getIntOrDefault(tag, "storedPower", 0);
        progress = NBTHelper.getIntOrDefault(tag, "progress", 0);
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    public int getPowerDrawPerTick(){
        return NovaGenetica.getMachineConfigMap().getOrDefault(blockId, NovaGenetica.getConfig().machinesConfig.defaultConfig).powerDrawPerTick;
    }

    public int getProcessingTime(){
        return NovaGenetica.getMachineConfigMap().getOrDefault(blockId, NovaGenetica.getConfig().machinesConfig.defaultConfig).processingTime;
    }
}
