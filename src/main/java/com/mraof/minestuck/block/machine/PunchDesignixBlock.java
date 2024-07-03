package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.CustomVoxelShape;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.machine.PunchDesignixBlockEntity;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PunchDesignixBlock extends MultiMachineBlock<PunchDesignixMultiblock> implements EditmodeDestroyable
{
	protected final Map<Direction, VoxelShape> shape;
	protected final BlockPos mainPos;
	
	public PunchDesignixBlock(PunchDesignixMultiblock machine, CustomVoxelShape shape, BlockPos pos, Properties properties)
	{
		super(machine, properties);
		this.shape = shape.createRotatedShapes();
		this.mainPos = pos;
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
		BlockPos mainPos = getMainPos(state, pos);
		if (level.getBlockEntity(mainPos) instanceof PunchDesignixBlockEntity designix)
			designix.onRightClick(player, state);
		return InteractionResult.SUCCESS;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			BlockPos mainPos = getMainPos(state, pos);
			if(level.getBlockEntity(mainPos) instanceof PunchDesignixBlockEntity designix)
			{
				designix.breakMachine();
				if(pos.equals(mainPos))
					designix.dropItem(true);
			}
			
			super.onRemove(state, level, pos, newState, isMoving);
		}
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
	
    /**
     *returns the block position of the "Main" block
     *aka the block with the BlockEntity for the machine
     *@param state the state of the block
     *@param pos the position the block
     */
	public BlockPos getMainPos(BlockState state, BlockPos pos)
	{
		Direction direction = state.getValue(FACING);
		Rotation rotation = MSRotationUtil.fromDirection(direction);
		
		return pos.offset(this.mainPos.rotate(rotation));
	}
	
	public static class Slot extends PunchDesignixBlock implements EntityBlock
	{
		public static final BooleanProperty HAS_CARD = MSProperties.HAS_CARD;
		
		public Slot(PunchDesignixMultiblock machine, CustomVoxelShape shape, Properties properties)
		{
			super(machine, shape, new BlockPos(0, 0, 0), properties);
			registerDefaultState(this.stateDefinition.any().setValue(HAS_CARD, false));
		}
		
		@Override
		protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
		{
			super.createBlockStateDefinition(builder);
			builder.add(HAS_CARD);
		}
		
		@Nullable
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
		{
			return new PunchDesignixBlockEntity(pos, state);
		}
	}
}
