package com.mraof.minestuck.util;

import net.minecraft.particles.IParticleData;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Supplier;

public class ParticlesAroundSolidBlock
{
	public static void spawnParticles(World worldIn, BlockPos pos, Supplier<IParticleData> particle)
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
