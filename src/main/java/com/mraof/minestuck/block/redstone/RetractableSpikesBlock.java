package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.util.MSDamageSources;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Acts similar to spike blocks, except the damage is dependent on whether the spikes are extended or not. Spikes will extend if the block is powered or if the block is in Pressure Sensitive mode and a mob has stepped on it.
 */
public class RetractableSpikesBlock extends Block
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty PRESSURE_SENSITIVE = BlockStateProperties.ENABLED;
	
	public RetractableSpikesBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(POWERED, false).setValue(PRESSURE_SENSITIVE, false));
	}
	
	@Override
	public void fallOn(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		if(worldIn.getBlockState(pos).getValue(POWERED) && fallDistance > 3)
		{
			entityIn.causeFallDamage(fallDistance, 3);
		} else
			super.fallOn(worldIn, pos, entityIn, fallDistance);
	}
	
	@Override
	public void stepOn(World worldIn, BlockPos pos, Entity entityIn)
	{
		BlockState state = worldIn.getBlockState(pos);
		
		if(entityIn instanceof LivingEntity)
		{
			tryExtendSpikes(worldIn, pos, state, state.getValue(PRESSURE_SENSITIVE));
			
			if(!worldIn.isClientSide && (entityIn.xOld != entityIn.getX() || entityIn.zOld != entityIn.getZ()) && state.getValue(POWERED))
			{
				double distanceX = Math.abs(entityIn.getX() - entityIn.xOld);
				double distanceZ = Math.abs(entityIn.getZ() - entityIn.zOld);
				if(distanceX >= (double) 0.003F || distanceZ >= (double) 0.003F)
				{
					entityIn.hurt(MSDamageSources.SPIKE, 1.0F); //TODO only activates for players when they take a running jump onto the block, works fine for other mobs
				}
			}
		}
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		super.tick(state, worldIn, pos, random);
		
		if(!worldIn.isClientSide)
		{
			boolean entityStandingOnBlock = false;
			AxisAlignedBB checkBB = new AxisAlignedBB(pos);
			List<LivingEntity> list = worldIn.getEntitiesOfClass(LivingEntity.class, checkBB.move(0, 0.5, 0));
			if(!list.isEmpty())
			{
				for(LivingEntity livingEntity : list)
				{
					entityStandingOnBlock = (livingEntity.isOnGround() && !livingEntity.isCrouching());
				}
			}
			
			if((state.getValue(POWERED) && !worldIn.hasNeighborSignal(pos) && !state.getValue(PRESSURE_SENSITIVE)) ||
					(state.getValue(POWERED) && !worldIn.hasNeighborSignal(pos) && !entityStandingOnBlock && state.getValue(PRESSURE_SENSITIVE)))
			{
				worldIn.setBlock(pos, state.setValue(POWERED, false), Constants.BlockFlags.BLOCK_UPDATE);
				worldIn.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
			} else
			{
				worldIn.getBlockTicks().scheduleTick(new BlockPos(pos), this, 20);
			}
		}
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS))
		{
			worldIn.setBlock(pos, state.cycle(PRESSURE_SENSITIVE), Constants.BlockFlags.DEFAULT);
			float pitch = state.getValue(PRESSURE_SENSITIVE) ? 1.5F : 0.5F;
			worldIn.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, pitch);
			
			return ActionResultType.SUCCESS;
		}
		
		return ActionResultType.PASS;
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		tryExtendSpikes(worldIn, pos, state, false);
	}
	
	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, worldIn, pos, oldState, isMoving);
		tryExtendSpikes(worldIn, pos, state, false);
	}
	
	/**
	 * Will extend the spikes(plays a sound and does a flat damage amount) assuming the block is powered or is being stepped on while in pressure sensitive mode
	 */
	public void tryExtendSpikes(World worldIn, BlockPos pos, BlockState state, boolean steppedOn)
	{
		if(!state.getValue(POWERED) && (worldIn.hasNeighborSignal(pos) || steppedOn))
		{
			worldIn.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.2F);
			AxisAlignedBB stabBB = new AxisAlignedBB(pos);
			List<LivingEntity> list = worldIn.getEntitiesOfClass(LivingEntity.class, stabBB.move(0, 0.5, 0));
			if(!list.isEmpty() && !worldIn.isClientSide)
			{
				for(LivingEntity livingEntity : list)
				{
					livingEntity.hurt(MSDamageSources.SPIKE, 4.0F);
				}
			}
			
			worldIn.setBlock(pos, state.setValue(POWERED, true), Constants.BlockFlags.BLOCK_UPDATE);
		}
		
		if(steppedOn || (worldIn.hasNeighborSignal(pos) && !state.getValue(POWERED)))
		{
			worldIn.setBlock(pos, state.setValue(POWERED, true), Constants.BlockFlags.BLOCK_UPDATE);
			worldIn.getBlockTicks().scheduleTick(new BlockPos(pos), this, 20);
		}
	}
	
	/**
	 * Helps entities avoid these blocks if possible should the spikes be out
	 */
	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity)
	{
		if(state.getValue(POWERED))
			return PathNodeType.DAMAGE_OTHER;
		else
			return PathNodeType.WALKABLE;
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
		builder.add(PRESSURE_SENSITIVE);
	}
}