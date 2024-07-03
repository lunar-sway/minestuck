package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.CustomVoxelShape;
import com.mraof.minestuck.block.EnumDowelType;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.machine.TotemLatheBlockEntity;
import com.mraof.minestuck.blockentity.machine.TotemLatheDowelBlockEntity;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

/**
 * Carves a cruxite dowel into a totem based on the card(s) put into the machines card slots. It has two block entities. One for the slot and one for dowel area which is rendered in a distinct way
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TotemLatheBlock extends MultiMachineBlock<TotemLatheMultiblock> implements EditmodeDestroyable
{
	protected final Map<Direction, VoxelShape> shape;
	protected final BlockPos mainPos;
	
	public TotemLatheBlock(TotemLatheMultiblock machine, CustomVoxelShape shape, BlockPos mainPos, Properties properties)
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
				&& !(otherState.isAir() || state.isAir())
				&& otherState.getValue(FACING) == state.getValue(FACING))
		{
			totemLathe.checkStates();
		}
		
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	@Override
	public void destroyFull(BlockState state, Level level, BlockPos pos)
	{
		var placement = this.machine.findPlacementFromSlot(level, this.getMainPos(state, pos));
		if(placement.isPresent())
			this.machine.removeAt(level, placement.get());
		else
		{
			for(var placementGuess : this.machine.guessPlacement(pos, state))
				this.machine.removeAt(level, placementGuess);
		}
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
	
	/**
	 * Holds the dowel and in the renderer is made to spin around when carving
	 */
	public static class DowelRod extends TotemLatheBlock implements EntityBlock
	{
		public static final EnumProperty<EnumDowelType> DOWEL = MSProperties.DOWEL_OR_NONE;
		protected final Map<Direction, VoxelShape> carvedShape;
		protected final Map<Direction, VoxelShape> dowelShape;
		
		public DowelRod(TotemLatheMultiblock machine, CustomVoxelShape emptyShape, CustomVoxelShape dowelShape, CustomVoxelShape carvedShape, BlockPos mainPos, Properties properties)
		{
			super(machine, emptyShape, mainPos, properties);
			this.dowelShape = dowelShape.createRotatedShapes();
			this.carvedShape = carvedShape.createRotatedShapes();
		}
		
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
		{
			if(state.getValue(DOWEL).equals(EnumDowelType.CARVED_DOWEL))
			{
				return carvedShape.get(state.getValue(FACING));
			} else if(state.getValue(DOWEL).equals(EnumDowelType.DOWEL))
			{
				return dowelShape.get(state.getValue(FACING));
			} else
			{
				return super.getShape(state, worldIn, pos, context);
			}
		}
		
		@Nullable
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
		{
			return new TotemLatheDowelBlockEntity(pos, state);
		}
		
		@Override
		protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
		{
			super.createBlockStateDefinition(builder);
			builder.add(DOWEL);
		}
		
		@Override
		public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
		{
			if(!newState.is(this) && level.getBlockEntity(pos) instanceof ItemStackBlockEntity blockEntity)
			{
				ItemStack stack = blockEntity.getStack();
				if(!stack.isEmpty())
				{
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
					blockEntity.setStack(ItemStack.EMPTY);
				}
			}
			
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}
	
	/**
	 * Holds the cards and keeps track of which stage the animations are at
	 */
	public static class Slot extends TotemLatheBlock implements EntityBlock
	{
		public static final IntegerProperty COUNT = MSProperties.COUNT_0_2;
		
		public Slot(TotemLatheMultiblock machine, CustomVoxelShape shape, Properties properties)
		{
			super(machine, shape, new BlockPos(0, 0, 0), properties);
		}
		
		@Nullable
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
		{
			return new TotemLatheBlockEntity(pos, state);
		}
		
		@Nullable
		@Override
		public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
		{
			return BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.TOTEM_LATHE.get(), TotemLatheBlockEntity::tick);
		}
		
		@Override
		protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
		{
			super.createBlockStateDefinition(builder);
			builder.add(COUNT);
		}
		
		@Override
		public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
		{
			if(!newState.is(state.getBlock()) &&
					level.getBlockEntity(pos) instanceof TotemLatheBlockEntity blockEntity)
				blockEntity.dropItems();
			
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}
}
