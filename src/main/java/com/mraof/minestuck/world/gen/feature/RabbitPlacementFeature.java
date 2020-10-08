package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class RabbitPlacementFeature extends Feature<NoFeatureConfig>
{
	public RabbitPlacementFeature(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean func_241855_a(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		if(!world.getBlockState(pos).getMaterial().isLiquid())
		{
			RabbitEntity rabbit = new RabbitEntity(EntityType.RABBIT, world.getWorld());
			rabbit.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			rabbit.onInitialSpawn(world, world.getDifficultyForLocation(rabbit.getPosition()), SpawnReason.CHUNK_GENERATION, null, null);
			world.addEntity(rabbit);
			return true;
		}
		return false;
	}
}