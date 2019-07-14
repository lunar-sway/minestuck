package com.mraof.minestuck.block;

import java.util.Map;

import javax.annotation.Nullable;

import com.mraof.minestuck.tileentity.HolopadTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class HolopadBlock extends MachineBlock
{
	public static final Map<Direction, VoxelShape> SHAPE = createRotatedShapes(2, 0, 1, 14, 6, 13);
	public static final Map<Direction, VoxelShape> COLLISION_SHAPE;
	protected static final AxisAlignedBB HOLOPAD_TOP_AABB = new AxisAlignedBB(3/16F, 6/16F, 2.6/16F, 13/16F, 7/16F, 12.6/16F);
	protected static final AxisAlignedBB HOLOPAD_CARDSLOT_AABB = new AxisAlignedBB(4/16F, 0F, 13.8/16F, 12/16F, 10.1/16F, 15.94/16F);
	
	public static final BooleanProperty HAS_CARD = MinestuckProperties.HAS_CARD;
	
	static
	{
		VoxelShape topShape = Block.makeCuboidShape(3, 6, 3, 13, 7, 13);
		COLLISION_SHAPE = createRotatedShapes(4, 0, 14, 12, 10, 16);
		COLLISION_SHAPE.replaceAll((enumFacing, shape) -> VoxelShapes.or(shape, topShape));
	}
	
	public HolopadBlock(Properties builder)
	{
		super(builder);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new HolopadTileEntity();
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(player.isSneaking()) return false;
		if(worldIn.isRemote)
			return true;
		TileEntity te = worldIn.getTileEntity(pos);
		
		if(te instanceof HolopadTileEntity)
			((HolopadTileEntity) te).onRightClick(player);
		return true;
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
	{
		HolopadTileEntity te = (HolopadTileEntity) worldIn.getTileEntity(pos);
		
		if(te != null && !worldIn.isRemote)
		{
			te.dropItem(true, worldIn, pos, te.getCard());
			te.destroyHologram(pos);
		}
		
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(HAS_CARD);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE.get(state.get(FACING));
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return COLLISION_SHAPE.get(state.get(FACING));
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}