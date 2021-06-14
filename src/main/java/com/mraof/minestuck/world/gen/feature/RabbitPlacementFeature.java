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
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		if(!world.getBlockState(pos).getMaterial().isLiquid())
		{
			RabbitEntity rabbit = new RabbitEntity(EntityType.RABBIT, world.getLevel());
			rabbit.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			rabbit.finalizeSpawn(world, world.getCurrentDifficultyAt(rabbit.blockPosition()), SpawnReason.CHUNK_GENERATION, null, null);
			world.addFreshEntity(rabbit);
			return true;
		}
		return false;
	}
}