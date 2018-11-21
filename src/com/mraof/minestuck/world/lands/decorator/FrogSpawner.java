package com.mraof.minestuck.world.lands.decorator;
 import java.util.Random;
 import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
 import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
 public class FrogSpawner extends BiomeSpecificDecorator
{
	int attempts;
 	public FrogSpawner(int attempts, Biome... biomes)
	{
		super(biomes);
		this.attempts = attempts;
	}
 	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		pos = world.getTopSolidOrLiquidBlock(pos);
 		if (!world.getBlockState(pos).getMaterial().isLiquid() && !provider.isPositionInSpawn(pos.getX(), pos.getZ()))
		{
			EntityFrog entity = new EntityFrog(world);
			entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			entity.onInitialSpawn(null, null);
			world.spawnEntity(entity);
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