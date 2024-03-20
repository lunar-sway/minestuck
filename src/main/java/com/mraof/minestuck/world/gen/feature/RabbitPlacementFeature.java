package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Objects;

public class RabbitPlacementFeature extends Feature<NoneFeatureConfiguration>
{
	public RabbitPlacementFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		
		Rabbit rabbit = Objects.requireNonNull(EntityType.RABBIT.create(level.getLevel()));
		rabbit.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		rabbit.finalizeSpawn(level, level.getCurrentDifficultyAt(rabbit.blockPosition()), MobSpawnType.CHUNK_GENERATION, null, null);
		level.addFreshEntity(rabbit);
		return true;
	}
}