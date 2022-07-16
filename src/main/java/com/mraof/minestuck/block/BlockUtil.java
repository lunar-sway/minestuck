package com.mraof.minestuck.block;

import net.minecraft.particles.IParticleData;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Supplier;

public class BlockUtil
{
	public static boolean hasSignalNotFromFacing(World worldIn, BlockPos pos, Direction stateFacing)
	{
		for(Direction direction : Direction.values()) //checks for a signal in any direction except the one it is facing
		{
			if(direction != stateFacing && worldIn.hasSignal(pos.relative(direction), direction))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static void spawnParticlesAroundSolidBlock(World worldIn, BlockPos pos, Supplier<IParticleData> particle)
	{
		Random random = worldIn.random;
		
		for(Direction direction : Direction.values())
		{
			BlockPos blockpos = pos.relative(direction);
			if(!worldIn.getBlockState(blockpos).isSolidRender(worldIn, blockpos)) //isSolidRender was isOpaqueCube
			{
				Direction.Axis axis = direction.getAxis();
				double xOffset = axis == Direction.Axis.X ? 0.5D + 9D / 16D * (double) direction.getStepX() : (double) random.nextFloat();
				double yOffset = axis == Direction.Axis.Y ? 0.5D + 9D / 16D * (double) direction.getStepY() : (double) random.nextFloat();
				double zOffset = axis == Direction.Axis.Z ? 0.5D + 9D / 16D * (double) direction.getStepZ() : (double) random.nextFloat();
				worldIn.addParticle(particle.get(), (double) pos.getX() + xOffset, (double) pos.getY() + yOffset, (double) pos.getZ() + zOffset, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}
