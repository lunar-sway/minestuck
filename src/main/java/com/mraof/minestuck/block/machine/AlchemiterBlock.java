package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.CustomVoxelShape;
import com.mraof.minestuck.block.EnumDowelType;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlchemiterBlock extends MachineBlock implements EditmodeDestroyable
{
	protected final Map<Direction, VoxelShape> shape;
	protected final boolean recursive, corner;
	protected final BlockPos mainPos;
	protected final AlchemiterMultiblock multiblock;
	
	public AlchemiterBlock(AlchemiterMultiblock multiblock, CustomVoxelShape shape, boolean recursive, boolean corner, BlockPos mainPos, Properties properties)
	{
		super(properties);
		this.multiblock = multiblock;
		this.shape = shape.createRotatedShapes();
		this.recursive = recursive;
		this.corner = corner;
		this.mainPos = mainPos;
	}
	
	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return shape.get(state.getValue(FACING));
	}
	
	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
	{
		Optional<BlockPos> mainPos = getMainPos(state, pos, level);
		
		if(mainPos.isPresent() && level.getBlockEntity(mainPos.get()) instanceof AlchemiterBlockEntity alchemiter)
		{
			alchemiter.onRightClick(level, player, state, hit.getDirection());
		}
		
		return InteractionResult.SUCCESS;
	}
	
	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			getMainPos(state, pos, level).ifPresent(mainPos -> {
				if(level.getBlockEntity(mainPos) instanceof AlchemiterBlockEntity alchemiter)
				{
					alchemiter.breakMachine();
					if(mainPos.equals(pos))
						alchemiter.dropItem(null);
				}
			});
			
			
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}
	
	@Override
	public void destroyFull(BlockState state, Level level, BlockPos pos)
	{
		var placement = this.getMainPos(state, pos, level)
				.flatMap(mainPos -> this.multiblock.findPlacementFromPad(level, mainPos));
		if(placement.isPresent())
			this.multiblock.removeAt(level, placement.get());
		else
		{
			for(var placementGuess : this.multiblock.guessPlacement(pos, state))
				this.multiblock.removeAt(level, placementGuess);
		}
	}
	
    /**
     * returns the block position of the "Main" block
     * aka the block with the BlockEntity for the machine
     */
	public Optional<BlockPos> getMainPos(BlockState state, BlockPos pos, BlockGetter level)
	{
		return getMainPos(state, pos, level, 4);
	}
	
	protected Optional<BlockPos> getMainPos(BlockState state, BlockPos pos, BlockGetter level, int count)
	{
		Direction direction = state.getValue(FACING);
		
		BlockPos newPos = pos.offset(mainPos.rotate(MSRotationUtil.fromDirection(direction)));
		
		if(!recursive)
			return Optional.of(newPos);
		else
		{
			BlockState newState = level.getBlockState(newPos);
			if(count > 0 && newState.getBlock() instanceof AlchemiterBlock && ((AlchemiterBlock) newState.getBlock()).corner
					&& newState.getValue(FACING).equals(this.corner ? state.getValue(FACING).getClockWise() : state.getValue(FACING)))
			{
				return ((AlchemiterBlock) newState.getBlock()).getMainPos(newState, newPos, level, count - 1);
			} else
				return Optional.empty();
		}
	}

	public static class Pad extends AlchemiterBlock implements EntityBlock
	{
		public static final EnumProperty<EnumDowelType> DOWEL = MSProperties.DOWEL_OR_NONE;
		private final Map<Direction, VoxelShape> dowelShape;
		
		public Pad(AlchemiterMultiblock machine, CustomVoxelShape emptyShape, CustomVoxelShape dowelShape, Properties properties)
		{
			super(machine, emptyShape, false, false, new BlockPos(0, 0, 0), properties);
			this.dowelShape = dowelShape.createRotatedShapes();
		}
		
		@Override
		protected VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
		{
			if(state.getValue(DOWEL).equals(EnumDowelType.NONE))
				return super.getShape(state, worldIn, pos, context);
			
			return dowelShape.get(state.getValue(FACING));
		}
		
		@Nullable
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
		{
			return new AlchemiterBlockEntity(pos, state);
		}
		
		@Override
		protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
		{
			super.createBlockStateDefinition(builder);
			builder.add(DOWEL);
		}
	}
}
