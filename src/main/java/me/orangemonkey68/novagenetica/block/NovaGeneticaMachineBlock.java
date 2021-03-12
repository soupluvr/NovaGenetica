package me.orangemonkey68.novagenetica.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class NovaGeneticaMachineBlock extends HorizontalFacingBlock implements BlockEntityProvider, InventoryProvider {
    private Supplier<BlockEntity> blockEntitySupplier;

    public NovaGeneticaMachineBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return blockEntitySupplier.get();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    //This is deprecated, but I don't know a better alternative
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        System.out.println(state.createScreenHandlerFactory(world, pos));
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity != null && blockEntity.getType() == this.blockEntitySupplier.get().getType() && blockEntity instanceof Inventory){
                ItemScatterer.spawn(world, pos, (Inventory)blockEntity);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return false;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return 0;
    }

    public void setBlockEntity(Supplier<BlockEntity> supplier){
        this.blockEntitySupplier = supplier;
    }

    @Override
    @Nullable
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity != null){
            if(entity instanceof InventoryProvider){
                return ((InventoryProvider)entity).getInventory(state, world, pos);
            }
        }
        return null;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof NamedScreenHandlerFactory){
            return (NamedScreenHandlerFactory) blockEntity;
        }
        return null;
    }
}
