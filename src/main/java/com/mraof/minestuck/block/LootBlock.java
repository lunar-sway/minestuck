package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.tileentity.LootTileEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

public class LootBlock extends Block
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public final Map<Direction, VoxelShape> shape;

    public LootBlock(Properties properties, CustomVoxelShape shape)
    {
        super(properties);
        this.shape = shape.createRotatedShapes();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getHeldItem(handIn);
        if (worldIn.isRemote) {
            return true;
        } else {
            if (itemstack.getItem() == Items.AIR){
                Direction direction = hit.getFace();
                Direction direction1 = direction.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : direction;
                worldIn.playSound((PlayerEntity)null, pos, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (getBlock() == MSBlocks.CALEDFWLCH_PEDESTAL_FULL){
                    worldIn.setBlockState(pos, MSBlocks.CALEDFWLCH_PEDESTAL_EMPTY.getDefaultState(), 11);
                    ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + 0.5D + (double)direction1.getXOffset() * 0.65D, (double)pos.getY() + 0.1D, (double)pos.getZ() + 0.5D + (double)direction1.getZOffset() * 0.65D, new ItemStack(MSItems.CALEDFWLCH, 1));
                    itementity.setMotion(0.05D * (double)direction1.getXOffset() + worldIn.rand.nextDouble() * 0.02D, 0.05D, 0.05D * (double)direction1.getZOffset() + worldIn.rand.nextDouble() * 0.02D);
                    worldIn.addEntity(itementity);

                    player.sendMessage(new StringTextComponent("You have claimed the legendary sword Caledfwlch! ...more like a legendary piece of shit."));
                }
                if (getBlock() == MSBlocks.LOOT_CHEST){
                    worldIn.setBlockState(pos, MSBlocks.LOOT_CHEST_EMPTY.getDefaultState().with(DecorBlock.FACING, direction1), 11);
                    //Empty chest should face direction of original block, not player oriented
                    //player.addItemStackToInventory(MSChestLootTables.MISC_POOL);
                }
                if (getBlock() == MSBlocks.WOODEN_LOOT_CHEST){
                    worldIn.setBlockState(pos, MSBlocks.LOOT_CHEST_EMPTY.getDefaultState().with(DecorBlock.FACING, direction1), 11);
                    ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + 0.5D + (double)direction1.getXOffset() * 0.65D, (double)pos.getY() + 0.1D, (double)pos.getZ() + 0.5D + (double)direction1.getZOffset() * 0.65D, new ItemStack(MSItems.BOONDOLLARS, 1));
                    itementity.setMotion(0.05D * (double)direction1.getXOffset() + worldIn.rand.nextDouble() * 0.02D, 0.05D, 0.05D * (double)direction1.getZOffset() + worldIn.rand.nextDouble() * 0.02D);
                    worldIn.addEntity(itementity);
                }
            }
            //on right clicking a block in a players range(onBlockActivated), an item should enter the players empty
            //hand accompanied by a block change and music, may also try and send a message of what loot they got
            return true;
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new LootTileEntity();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public float getExplosionResistance(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity exploder, Explosion explosion)
    {
        return 3600000.0F;
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        return Block.doesSideFillSquare(state.getCollisionShape(worldIn, pos.down()), Direction.UP);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return shape.get(state.get(FACING));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }
}
