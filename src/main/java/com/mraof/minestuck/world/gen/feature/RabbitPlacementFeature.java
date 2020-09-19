package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

public class RabbitPlacementFeature extends Feature<NoFeatureConfig>
{
	public RabbitPlacementFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		if(!worldIn.getBlockState(pos).getMaterial().isLiquid())
		{
			RabbitEntity rabbit = new RabbitEntity(EntityType.RABBIT, worldIn.getWorld());
			rabbit.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			rabbit.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(new BlockPos(rabbit)), SpawnReason.CHUNK_GENERATION, null, null);
			worldIn.addEntity(rabbit);
			return true;
		}
		return false;
	}
}