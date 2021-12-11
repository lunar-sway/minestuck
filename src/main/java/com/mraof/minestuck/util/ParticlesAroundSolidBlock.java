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
		Random random = worldIn.rand;
		
		for(Direction direction : Direction.values())
		{
			BlockPos blockpos = pos.offset(direction);
			if(!worldIn.getBlockState(blockpos).isOpaqueCube(worldIn, blockpos))
			{
				Direction.Axis direction$axis = direction.getAxis();
				double xOffset = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double) direction.getXOffset() : (double) random.nextFloat();
				double yOffset = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double) direction.getYOffset() : (double) random.nextFloat();
				double zOffset = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double) direction.getZOffset() : (double) random.nextFloat();
				worldIn.addParticle(particle.get(), (double) pos.getX() + xOffset, (double) pos.getY() + yOffset, (double) pos.getZ() + zOffset, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}
