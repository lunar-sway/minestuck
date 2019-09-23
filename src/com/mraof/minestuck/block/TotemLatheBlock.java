package com.mraof.minestuck.block;

import com.mraof.minestuck.block.multiblock.MachineMultiblock;
import com.mraof.minestuck.tileentity.ItemStackTileEntity;
import com.mraof.minestuck.tileentity.TotemLatheTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
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

public class TotemLatheBlock extends MultiMachineBlock
{
	public static final Map<Direction, VoxelShape> CARD_SLOT_SHAPE = createRotatedShapes(0, 0, 3, 16, 16, 15);
	public static final Map<Direction, VoxelShape> BOTTOM_LEFT_SHAPE = createRotatedShapes(0, 0, 4, 16, 16, 16);
	public static final Map<Direction, VoxelShape> BOTTOM_RIGHT_SHAPE = createRotatedShapes(0, 0, 4, 16, 12, 16);
	public static final Map<Direction, VoxelShape> BOTTOM_CORNER_SHAPE = createRotatedShapes(4, 0, 5, 16, 16, 15);
	public static final Map<Direction, VoxelShape> MIDDLE_SHAPE = createRotatedShapes(0, 0, 4, 16, 16, 15);
	public static final Map<Direction, VoxelShape> WHEEL_SHAPE = createRotatedShapes(0, 0, 7, 14, 10, 13);
	public static final Map<Direction, VoxelShape> ROD_SHAPE = createRotatedShapes(0, 4, 7, 16, 10, 13);
	public static final Map<Direction, VoxelShape> TOP_CORNER_SHAPE = createRotatedShapes(0, 0, 5, 16, 16, 15);
	public static final Map<Direction, VoxelShape> TOP_SHAPE = createRotatedShapes(0, 3, 5, 16, 16, 15);
	public static final Map<Direction, VoxelShape> CARVER_SHAPE = createRotatedShapes(6, 0, 5, 16, 16, 15);
	
	
	protected final Map<Direction, VoxelShape> shape;
	protected final BlockPos mainPos;
	
	public TotemLatheBlock(MachineMultiblock machine, Map<Direction, VoxelShape> shape, BlockPos mainPos, Properties properties)
	{
		super(machine, properties);
		this.shape = shape;
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
		if(worldIn.isRemote)
			return true;
		
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if(te instanceof TotemLatheTileEntity)
			((TotemLatheTileEntity) te).onRightClick(player, state);
		return true;
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(!hasTileEntity(state))
		{
			BlockPos mainPos = getMainPos(state, pos);
			TileEntity te = worldIn.getTileEntity(mainPos);
			BlockState otherState = worldIn.getBlockState(mainPos);
			if(te instanceof TotemLatheTileEntity && otherState.get(FACING) == state.get(FACING))
			{
				((TotemLatheTileEntity) te).setBroken();
			}
		}
		
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TotemLatheTileEntity)
			((TotemLatheTileEntity) te).checkStates();
	}
	
    /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     */
	public BlockPos getMainPos(BlockState state, BlockPos pos)
	{
		Rotation rotation = rotationFromDirection(state.get(FACING));
		
		return pos.add(mainPos.rotate(rotation));
	}
	
	public static class Rod extends TotemLatheBlock
	{
		public static final BooleanProperty ACTIVE = MSProperties.ACTIVE;
		
		public Rod(MachineMultiblock machine, Map<Direction, VoxelShape> shape, BlockPos mainPos, Properties properties)
		{
			super(machine, shape, mainPos, properties);
		}
		
		@Override
		protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
		{
			super.fillStateContainer(builder);
			builder.add(ACTIVE);
		}
	}
	
	public static class DowelRod extends TotemLatheBlock
	{
		public static final EnumProperty<EnumDowelType> DOWEL = MSProperties.DOWEL;
		
		public DowelRod(MachineMultiblock machine, Map<Direction, VoxelShape> shape, BlockPos mainPos, Properties properties)
		{
			super(machine, shape, mainPos, properties);
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
			return new ItemStackTileEntity();
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
	
	public static class Slot extends TotemLatheBlock
	{
		public static final IntegerProperty COUNT = MSProperties.COUNT_0_2;
		
		public Slot(MachineMultiblock machine, Map<Direction, VoxelShape> shape, Properties properties)
		{
			super(machine, shape, new BlockPos(0, 0, 0), properties);
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
			return new TotemLatheTileEntity();
		}
		
		@Override
		protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
		{
			super.fillStateContainer(builder);
			builder.add(COUNT);
		}
		
		@Override
		public BlockRenderLayer getRenderLayer()
		{
			return BlockRenderLayer.CUTOUT_MIPPED;
		}
	}
}