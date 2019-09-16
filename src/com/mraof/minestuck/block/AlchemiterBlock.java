package com.mraof.minestuck.block;

import com.mraof.minestuck.block.multiblock.MachineMultiblock;
import com.mraof.minestuck.tileentity.AlchemiterTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

public class AlchemiterBlock extends MultiMachineBlock
{
	public static final Map<Direction, VoxelShape> FULL_BLOCK_SHAPE = createRotatedShapes(0, 0, 0, 16, 16, 16);
	public static final Map<Direction, VoxelShape> TOTEM_PAD_SHAPE = createRotatedShapes(8, 0, 2, 14, 16, 16);
	public static final Map<Direction, VoxelShape> LOWER_ROD_SHAPE = createRotatedShapes(10, 0, 2, 14, 16, 16);
	public static final Map<Direction, VoxelShape> UPPER_ROD_SHAPE = createRotatedShapes(7, 0, 2, 14, 10, 16);
	
	protected final Map<Direction, VoxelShape> shape;
	protected final boolean recursive, corner;
	protected final BlockPos mainPos;
	
	public AlchemiterBlock(MachineMultiblock machine, Map<Direction, VoxelShape> shape, boolean recursive, boolean corner, BlockPos mainPos, Properties properties)
	{
		super(machine, properties);
		this.shape = shape;
		this.recursive = recursive;
		this.corner = corner;
		this.mainPos = mainPos;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return shape.get(state.get(FACING));
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		BlockPos mainPos = getMainPos(state, pos, worldIn);
		TileEntity te = worldIn.getTileEntity(mainPos);
		
		if (te instanceof AlchemiterTileEntity)
		{
			((AlchemiterTileEntity) te).onRightClick(worldIn, player, state, hit.getFace());
		}
		
		return true;
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			BlockPos mainPos = getMainPos(state, pos, worldIn);
			TileEntity te = worldIn.getTileEntity(mainPos);
			if(te instanceof AlchemiterTileEntity)
			{
				AlchemiterTileEntity alchemiter = (AlchemiterTileEntity) te;
				alchemiter.breakMachine();
				if(mainPos.equals(pos))
					alchemiter.dropItem(null);
			}
			
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
	
    /**
     * returns the block position of the "Main" block
     * aka the block with the TileEntity for the machine
     */
	public BlockPos getMainPos(BlockState state, BlockPos pos, IBlockReader world)
	{
		return getMainPos(state, pos, world, 4);
	}
	
	protected BlockPos getMainPos(BlockState state, BlockPos pos, IBlockReader world, int count)
	{
		Direction direction = state.get(FACING);
		
		BlockPos newPos = pos.add(mainPos.rotate(rotationFromDirection(direction)));
		
		if(!recursive)
			return newPos;
		else
		{
			BlockState newState = world.getBlockState(newPos);
			if(count > 0 && newState.getBlock() instanceof AlchemiterBlock && ((AlchemiterBlock) newState.getBlock()).corner
					&& newState.get(FACING).equals(this.corner ? state.get(FACING).rotateY() : state.get(FACING)))
			{
				return ((AlchemiterBlock) newState.getBlock()).getMainPos(newState, newPos, world, count - 1);
			} else return new BlockPos(0, -1 , 0);
		}
	}

	public static class Pad extends AlchemiterBlock
	{
		public static final EnumProperty<EnumDowelType> DOWEL = MSProperties.DOWEL_OR_NONE;
		
		public Pad(MachineMultiblock machine, Map<Direction, VoxelShape> shape, Properties properties)
		{
			super(machine, shape, false, false, new BlockPos(0, 0, 0), properties);
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
			return new AlchemiterTileEntity();
		}
		
		@Override
		protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
		{
			super.fillStateContainer(builder);
			builder.add(DOWEL);
		}
		
		@Override
		public BlockRenderLayer getRenderLayer()
		{
			return BlockRenderLayer.CUTOUT;
		}
	}
}