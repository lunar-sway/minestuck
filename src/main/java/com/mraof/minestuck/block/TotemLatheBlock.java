package com.mraof.minestuck.block;

import com.mraof.minestuck.block.multiblock.MachineMultiblock;
import com.mraof.minestuck.tileentity.ItemStackTileEntity;
import com.mraof.minestuck.tileentity.TotemLatheTileEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
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
	protected final Map<Direction, VoxelShape> shape;
	protected final BlockPos mainPos;
	
	public TotemLatheBlock(MachineMultiblock machine, CustomVoxelShape shape, BlockPos mainPos, Properties properties)
	{
		super(machine, properties);
		this.shape = shape.createRotatedShapes();
		this.mainPos = mainPos;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return shape.get(state.get(FACING));
	}
	
	@Override
	@SuppressWarnings("deprecation")
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
	@SuppressWarnings("deprecation")
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
	@SuppressWarnings("deprecation")
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
		Rotation rotation = MSRotationUtil.fromDirection(state.get(FACING));
		
		return pos.add(mainPos.rotate(rotation));
	}
	
	public static class Rod extends TotemLatheBlock
	{
		public static final BooleanProperty ACTIVE = MSProperties.ACTIVE;
		protected final Map<Direction, VoxelShape> activeShape;
		
		public Rod(MachineMultiblock machine, CustomVoxelShape shape, CustomVoxelShape activeShape, BlockPos mainPos, Properties properties)
		{
			super(machine, shape, mainPos, properties);
			this.activeShape = activeShape.createRotatedShapes();
		}
		
		@Override
		protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
		{
			super.fillStateContainer(builder);
			builder.add(ACTIVE);
		}
		
		@Override
		public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
		{
			return state.get(ACTIVE) ? activeShape.get(state.get(FACING)) : super.getShape(state, worldIn, pos, context);
		}
	}
	
	public static class DowelRod extends TotemLatheBlock
	{
		public static final EnumProperty<EnumDowelType> DOWEL = MSProperties.DOWEL;
		protected final Map<Direction, VoxelShape> carvedShape;
		
		public DowelRod(MachineMultiblock machine, CustomVoxelShape shape, CustomVoxelShape carvedShape, BlockPos mainPos, Properties properties)
		{
			super(machine, shape, mainPos, properties);
			this.carvedShape = carvedShape.createRotatedShapes();
		}
		
		@Override
		public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
		{
			return state.get(DOWEL).equals(EnumDowelType.CARVED_DOWEL) ? carvedShape.get(state.get(FACING)) : super.getShape(state, worldIn, pos, context);
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
		
		public Slot(MachineMultiblock machine, CustomVoxelShape shape, Properties properties)
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