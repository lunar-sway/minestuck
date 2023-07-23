package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.util.MSDamageSources;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Acts similar to spike blocks, except the damage is dependent on whether the spikes are extended or not. Spikes will extend if the block is powered or if the block is in Pressure Sensitive mode and a mob has stepped on it.
 */
@ParametersAreNonnullByDefault
public class RetractableSpikesBlock extends Block
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty PRESSURE_SENSITIVE = MSProperties.MACHINE_TOGGLE;
	
	public RetractableSpikesBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(POWERED, false).setValue(PRESSURE_SENSITIVE, false));
	}
	
	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entityIn, float fallDistance)
	{
		if(state.getValue(POWERED) && fallDistance > 3)
		{
			entityIn.causeFallDamage(fallDistance, 3, level.damageSources().fall());
		} else
			super.fallOn(level, state, pos, entityIn, fallDistance);
	}
	
	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entityIn)
	{
		if(entityIn instanceof LivingEntity)
		{
			tryExtendSpikes(level, pos, state, state.getValue(PRESSURE_SENSITIVE));
			
			if(!level.isClientSide && (entityIn.xOld != entityIn.getX() || entityIn.zOld != entityIn.getZ()) && state.getValue(POWERED))
			{
				double distanceX = Math.abs(entityIn.getX() - entityIn.xOld);
				double distanceZ = Math.abs(entityIn.getZ() - entityIn.zOld);
				if(distanceX >= (double) 0.003F || distanceZ >= (double) 0.003F)
				{
					entityIn.hurt(MSDamageSources.spike(level.registryAccess()), 1.0F); //TODO only activates for players when they take a running jump onto the block, works fine for other mobs
				}
			}
		}
	}
	
	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random)
	{
		super.tick(state, worldIn, pos, random);
		
		if(!worldIn.isClientSide)
		{
			boolean entityStandingOnBlock = false;
			AABB checkBB = new AABB(pos);
			List<LivingEntity> list = worldIn.getEntitiesOfClass(LivingEntity.class, checkBB.move(0, 0.5, 0));
			if(!list.isEmpty())
			{
				for(LivingEntity livingEntity : list)
				{
					entityStandingOnBlock = (livingEntity.onGround() && !livingEntity.isCrouching());
				}
			}
			
			if((state.getValue(POWERED) && !worldIn.hasNeighborSignal(pos) && !state.getValue(PRESSURE_SENSITIVE)) ||
					(state.getValue(POWERED) && !worldIn.hasNeighborSignal(pos) && !entityStandingOnBlock && state.getValue(PRESSURE_SENSITIVE)))
			{
				worldIn.setBlock(pos, state.setValue(POWERED, false), Block.UPDATE_CLIENTS);
				worldIn.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, 1.2F);
			} else
			{
				worldIn.scheduleTick(new BlockPos(pos), this, 20);
			}
		}
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS))
		{
			level.setBlock(pos, state.cycle(PRESSURE_SENSITIVE), Block.UPDATE_ALL);
			float pitch = state.getValue(PRESSURE_SENSITIVE) ? 1.5F : 0.5F;
			level.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 0.5F, pitch);
			
			return InteractionResult.SUCCESS;
		}
		
		return InteractionResult.PASS;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, level, pos, blockIn, fromPos, isMoving);
		tryExtendSpikes(level, pos, state, false);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, level, pos, oldState, isMoving);
		tryExtendSpikes(level, pos, state, false);
	}
	
	/**
	 * Will extend the spikes(plays a sound and does a flat damage amount) assuming the block is powered or is being stepped on while in pressure sensitive mode
	 */
	public void tryExtendSpikes(Level level, BlockPos pos, BlockState state, boolean steppedOn)
	{
		if(!state.getValue(POWERED) && (level.hasNeighborSignal(pos) || steppedOn))
		{
			level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, 1.2F);
			AABB stabBB = new AABB(pos);
			List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, stabBB.move(0, 0.5, 0));
			if(!list.isEmpty() && !level.isClientSide)
			{
				for(LivingEntity livingEntity : list)
				{
					livingEntity.hurt(MSDamageSources.spike(level.registryAccess()), 4.0F);
				}
			}
			
			level.setBlock(pos, state.setValue(POWERED, true), Block.UPDATE_CLIENTS);
		}
		
		if(steppedOn || (level.hasNeighborSignal(pos) && !state.getValue(POWERED)))
		{
			level.setBlock(pos, state.setValue(POWERED, true), Block.UPDATE_CLIENTS);
			level.scheduleTick(new BlockPos(pos), this, 20);
		}
	}
	
	/**
	 * Helps entities avoid these blocks if possible should the spikes be out
	 */
	@Nullable
	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity)
	{
		if(state.getValue(POWERED))
			return BlockPathTypes.DAMAGE_OTHER;
		else
			return BlockPathTypes.WALKABLE;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
		builder.add(PRESSURE_SENSITIVE);
	}
}