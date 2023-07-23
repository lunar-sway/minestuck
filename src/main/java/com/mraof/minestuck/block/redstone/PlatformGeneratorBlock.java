package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.redstone.PlatformGeneratorBlockEntity;
import com.mraof.minestuck.effects.CreativeShockEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * When powered, the block entity creates a line of platform blocks in the direction it is facing.
 * These blocks will generate even if there is a physical barrier between the generator and the end of the line, but only replace air or fluid blocks.
 * Right clicking the block toggles whether the generated platform blocks are visible
 */
public class PlatformGeneratorBlock extends MSDirectionalBlock implements EntityBlock
{
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED; //used for texture purposes
	public static final BooleanProperty INVISIBLE_MODE = MSProperties.MACHINE_TOGGLE;
	
	public static final int MAX_GENERATOR_REACH = 16;
	
	public PlatformGeneratorBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWER, 0).setValue(POWERED, false).setValue(INVISIBLE_MODE, false));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS))
		{
			level.setBlock(pos, state.cycle(INVISIBLE_MODE), Block.UPDATE_ALL);
			level.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 0.5F, state.getValue(INVISIBLE_MODE) ? 1.5F : 0.5F);
			
			return InteractionResult.SUCCESS;
		}
		
		return InteractionResult.PASS;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new PlatformGeneratorBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
	{
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.PLATFORM_GENERATOR.get(), PlatformGeneratorBlockEntity::serverTick) : null;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, level, pos, blockIn, fromPos, isMoving);
		updatePower(level, pos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, level, pos, oldState, isMoving);
		updatePower(level, pos);
	}
	
	public void updatePower(Level level, BlockPos pos)
	{
		if(!level.isClientSide)
		{
			BlockState oldState = level.getBlockState(pos);
			int newPower = level.getBestNeighborSignal(pos);
			
			BlockState newState = setPower(oldState, newPower);
			if(oldState != newState)
				level.setBlockAndUpdate(pos, newState);
		}
	}
	
	public static BlockState setPower(BlockState state, int newPower)
	{
		return state.setValue(POWER, newPower).setValue(POWERED, newPower > 0);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		super.onRemove(state, level, pos, newState, isMoving);
		
		Direction facing = state.getValue(FACING);
		for(int blockIterate = 1; blockIterate < MAX_GENERATOR_REACH; blockIterate++)
		{
			BlockPos iteratePos = new BlockPos(pos.relative(facing, blockIterate));
			
			if(!level.isAreaLoaded(pos, blockIterate) || level.isOutsideBuildHeight(iteratePos.getY()))
				break;
			
			BlockState iterateBlockState = level.getBlockState(iteratePos);
			
			if(iterateBlockState.getBlock() instanceof PlatformBlock)
				PlatformBlock.updateSurvival(iterateBlockState, level, iteratePos);
		}
	}
	
	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand)
	{
		if(rand.nextInt(15) < stateIn.getValue(POWER))
		{
			BlockUtil.spawnParticlesAroundSolidBlock(level, pos, () -> DustParticleOptions.REDSTONE);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWER);
		builder.add(POWERED);
		builder.add(INVISIBLE_MODE);
	}
}
