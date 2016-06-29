package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class RabbitSpawner implements ILandDecorator
{
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		for(int i = 0; i < 8; i++)
			if(random.nextDouble() < 0.2)
			{
				int x = random.nextInt(16) + (chunkX << 4) + 8;
				int z = random.nextInt(16) + (chunkZ << 4) + 8;
				BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));
				if(world.getBlockState(pos).getMaterial().isLiquid() || provider.isPositionInSpawn(x, z) || world.getBiomeForCoordsBody(pos) == BiomeMinestuck.mediumOcean)
					continue;
				
				EntityRabbit entity = new EntityRabbit(world);
				entity.setPosition(x + 0.5, pos.getY(), z + 0.5);
				entity.onInitialSpawn(null, null);
				world.spawnEntityInWorld(entity);
			}
		return null;
	}
	
	@Override
	public float getPriority()
	{
		return 0.2F;
	}
	
}
