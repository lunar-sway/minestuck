package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.world.gen.Heightmap;

public class RabbitSpawner extends BiomeSpecificDecorator
{
	int attempts;

	public RabbitSpawner(int attempts, Biome... biomes)
	{
		super(biomes);
		this.attempts = attempts;
	}

	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		pos = world.getHeight(Heightmap.Type.MOTION_BLOCKING, pos);

		if (!world.getBlockState(pos).getMaterial().isLiquid() )//&& !provider.isPositionInSpawn(pos.getX(), pos.getZ()))
		{
			RabbitEntity entity = EntityType.RABBIT.create(world);
			entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			entity.onInitialSpawn(world, null, SpawnReason.CHUNK_GENERATION, null, null);
			world.addEntity(entity);
		}
		
		return null;
	}

	@Override
	public int getCount(Random random)
	{
		int count = 0;
		for (int i = 0; i < attempts; i++)
			if (random.nextDouble() < 0.2)
				count++;
		return count;
	}

	@Override
	public float getPriority()
	{
		return 0.2F;
	}

}
