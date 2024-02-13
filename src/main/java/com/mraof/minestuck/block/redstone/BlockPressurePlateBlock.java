package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Gives off a full power redstone signal if a block is resting above it or a player is standing on it without crouching
 */
public class BlockPressurePlateBlock extends Block
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	
	public BlockPressurePlateBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(POWERED, false));
	}
	
	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entityIn)
	{
		if(entityIn instanceof Player)
		{
			tryDepressPlate(level, pos, state);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		super.tick(state, level, pos, random);
		
		if(!level.isClientSide)
		{
			boolean entityStandingOnBlock = isPlayerStandingAbove(level, pos);
			
			if(!entityStandingOnBlock && !isAboveBlockFullyTouching(level, pos.above()) && state.getValue(POWERED))
			{
				level.setBlock(pos, state.setValue(POWERED, false), Block.UPDATE_ALL);
				level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, 1.2F);
			} else if(entityStandingOnBlock)
			{
				level.scheduleTick(new BlockPos(pos), this, 20);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return state.getValue(POWERED);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side)
	{
		return blockState.getValue(POWERED) ? 15 : 0;
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, level, pos, blockIn, fromPos, isMoving);
		tryDepressPlate(level, pos, state);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, level, pos, oldState, isMoving);
		tryDepressPlate(level, pos, state);
	}
	
	public static boolean isAboveBlockFullyTouching(Level level, BlockPos abovePos)
	{
		return level.getBlockState(abovePos).isFaceSturdy(level, abovePos, Direction.DOWN);
	}
	
	public static boolean isPlayerStandingAbove(Level level, BlockPos pos)
	{
		AABB checkBB = new AABB(pos);
		List<Player> list = level.getEntitiesOfClass(Player.class, checkBB.move(0, 0.5, 0));
		return list.stream().anyMatch(playerEntity -> playerEntity.onGround() && !playerEntity.isCrouching());
	}
	
	/**
	 * Will depress the plate assuming the block is being stepped on by a player or while a suitable block is above it, causing it to become powered
	 */
	public void tryDepressPlate(Level level, BlockPos pos, BlockState state)
	{
		BlockPos abovePos = pos.above();
		if((isAboveBlockFullyTouching(level, abovePos) || isPlayerStandingAbove(level, pos)) && !state.getValue(POWERED))
		{
			level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, 1.2F);
			level.setBlock(pos, state.setValue(POWERED, true), Block.UPDATE_ALL);
		}
		
		level.scheduleTick(new BlockPos(pos), this, 20);
	}
	
	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand)
	{
		if(stateIn.getValue(POWERED))
			BlockUtil.spawnParticlesAroundSolidBlock(level, pos, () -> DustParticleOptions.REDSTONE);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
	}
}