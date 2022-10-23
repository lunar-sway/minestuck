package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class FloorCogFeature extends AbstractTemplateFeature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_LARGE_FLOOR_COG_1 = new ResourceLocation(Minestuck.MOD_ID, "large_floor_cog_1");
	private static final ResourceLocation STRUCTURE_LARGE_FLOOR_COG_2 = new ResourceLocation(Minestuck.MOD_ID, "large_floor_cog_2");
	
	public FloorCogFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	protected ResourceLocation pickTemplate(Random random)
	{
		return random.nextBoolean() ? STRUCTURE_LARGE_FLOOR_COG_1 : STRUCTURE_LARGE_FLOOR_COG_2;
	}
	
	@Override
	protected int pickY(WorldGenLevel level, BlockPos pos, Vec3i templateSize, Random random)
	{
		int yMin = level.getMaxBuildHeight(), yMax = level.getMinBuildHeight();
		for(BlockPos floorPos : BlockPos.betweenClosed(0, 0, 0, templateSize.getX(), 0, templateSize.getZ()))
		{
			int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX() + floorPos.getX(), pos.getZ() + floorPos.getZ());
			yMax = Math.max(yMax, y);
			yMin = Math.min(yMin, y);
		}
		
		if(yMin == yMax)
			return yMin - templateSize.getY() + 1;
		else
			return yMin - templateSize.getY() + 2;
	}
}