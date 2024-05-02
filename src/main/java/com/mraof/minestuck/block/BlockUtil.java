package com.mraof.minestuck.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BlockUtil
{
	public static boolean hasSignalNotFromFacing(Level level, BlockPos pos, Direction stateFacing)
	{
		for(Direction direction : Direction.values()) //checks for a signal in any direction except the one it is facing
		{
			if(direction != stateFacing && level.hasSignal(pos.relative(direction), direction))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Expanded variety of canBeReplaced
	 */
	public static boolean isReplaceable(BlockState state)
	{
		return state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced();
	}
	
	public static void spawnParticlesAroundSolidBlock(Level level, BlockPos pos, Supplier<ParticleOptions> particle)
	{
		RandomSource random = level.random;
		
		for(Direction direction : Direction.values())
		{
			BlockPos blockpos = pos.relative(direction);
			if(!level.getBlockState(blockpos).isSolidRender(level, blockpos)) //isSolidRender was isOpaqueCube
			{
				Direction.Axis axis = direction.getAxis();
				double xOffset = axis == Direction.Axis.X ? 0.5D + 9D / 16D * (double) direction.getStepX() : (double) random.nextFloat();
				double yOffset = axis == Direction.Axis.Y ? 0.5D + 9D / 16D * (double) direction.getStepY() : (double) random.nextFloat();
				double zOffset = axis == Direction.Axis.Z ? 0.5D + 9D / 16D * (double) direction.getStepZ() : (double) random.nextFloat();
				level.addParticle(particle.get(), (double) pos.getX() + xOffset, (double) pos.getY() + yOffset, (double) pos.getZ() + zOffset, 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	public static <T extends BlockEntity, P extends BlockEntity> BlockEntityTicker<P> checkTypeForTicker(BlockEntityType<P> placedType, BlockEntityType<T> tickerType, BlockEntityTicker<? super T> ticker)
	{
		return tickerType == placedType ? (BlockEntityTicker<P>) ticker : null;
	}
}
