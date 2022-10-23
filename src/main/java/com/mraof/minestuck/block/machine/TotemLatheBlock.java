package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.EnumDowelType;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import com.mraof.minestuck.blockentity.machine.TotemLatheBlockEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

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
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return shape.get(state.getValue(FACING));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
	{
		if(level.isClientSide)
			return InteractionResult.SUCCESS;
		
		BlockPos mainPos = getMainPos(state, pos);
		if(level.getBlockEntity(mainPos) instanceof TotemLatheBlockEntity totemLathe)
			totemLathe.onRightClick(player, state);
		return InteractionResult.SUCCESS;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		BlockPos mainPos = getMainPos(state, pos);
		BlockState otherState = level.getBlockState(mainPos);
		if(level.getBlockEntity(mainPos) instanceof TotemLatheBlockEntity totemLathe
				&& otherState.getValue(FACING) == state.getValue(FACING))
		{
			totemLathe.checkStates();
		}
		
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		if(level.getBlockEntity(pos) instanceof TotemLatheBlockEntity totemLathe)
			totemLathe.checkStates();
	}
	
    /**
     *returns the block position of the "Main" block
     *aka the block with the BlockEntity for the machine
     */
	public BlockPos getMainPos(BlockState state, BlockPos pos)
	{
		Rotation rotation = MSRotationUtil.fromDirection(state.getValue(FACING));
		
		return pos.offset(mainPos.rotate(rotation));
	}
	
	public static class Rod extends TotemLatheBlock
	{
		public static final BooleanProperty ACTIVE = MSProperties.ACTIVE;
		protected final Map<Direction, VoxelShape> activeShape;
		
		public Rod(MachineMultiblock machine, CustomVoxelShape shape, CustomVoxelShape activeShape, BlockPos mainPos, Properties properties)
		{
			super(machine, shape, mainPos, properties);
			this.activeShape = activeShape.createRotatedShapes();
			this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false));
		}
		
		@Override
		protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
		{
			super.createBlockStateDefinition(builder);
			builder.add(ACTIVE);
		}
		
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
		{
			return state.getValue(ACTIVE) ? activeShape.get(state.getValue(FACING)) : super.getShape(state, worldIn, pos, context);
		}
	}
	
	public static class DowelRod extends TotemLatheBlock implements EntityBlock
	{
		public static final EnumProperty<EnumDowelType> DOWEL = MSProperties.DOWEL;
		protected final Map<Direction, VoxelShape> carvedShape;
		
		public DowelRod(MachineMultiblock machine, CustomVoxelShape shape, CustomVoxelShape carvedShape, BlockPos mainPos, Properties properties)
		{
			super(machine, shape, mainPos, properties);
			this.carvedShape = carvedShape.createRotatedShapes();
		}
		
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
		{
			return state.getValue(DOWEL).equals(EnumDowelType.CARVED_DOWEL) ? carvedShape.get(state.getValue(FACING)) : super.getShape(state, worldIn, pos, context);
		}
		
		@Nullable
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
		{
			return new ItemStackBlockEntity(pos, state);
		}
		
		@Override
		protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
		{
			super.createBlockStateDefinition(builder);
			builder.add(DOWEL);
		}

//		@Override
//		public BlockRenderLayer getRenderLayer()
//		{
//			return BlockRenderLayer.CUTOUT;
//		}
	}
	
	public static class Slot extends TotemLatheBlock implements EntityBlock
	{
		public static final IntegerProperty COUNT = MSProperties.COUNT_0_2;
		
		public Slot(MachineMultiblock machine, CustomVoxelShape shape, Properties properties)
		{
			super(machine, shape, new BlockPos(0, 0, 0), properties);
		}
		
		@Nullable
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
		{
			return new TotemLatheBlockEntity(pos, state);
		}
		
		@Override
		protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
		{
			super.createBlockStateDefinition(builder);
			builder.add(COUNT);
		}
	}
}