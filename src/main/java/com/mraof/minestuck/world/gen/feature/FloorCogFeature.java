package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FloorCogFeature extends AbstractTemplateFeature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_LARGE_FLOOR_COG_1 = new ResourceLocation(Minestuck.MOD_ID, "large_floor_cog_1");
	private static final ResourceLocation STRUCTURE_LARGE_FLOOR_COG_2 = new ResourceLocation(Minestuck.MOD_ID, "large_floor_cog_2");
	
	public FloorCogFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	protected ResourceLocation pickTemplate(RandomSource random)
	{
		return random.nextBoolean() ? STRUCTURE_LARGE_FLOOR_COG_1 : STRUCTURE_LARGE_FLOOR_COG_2;
	}
	
	@Override
	protected int pickY(WorldGenLevel level, TemplatePlacement placement, RandomSource random)
	{
		TemplatePlacement.Range heightRange = placement.heightRange(Heightmap.Types.WORLD_SURFACE_WG, level);
		
		if(heightRange.min() == heightRange.max())
			return heightRange.min() - placement.size().getY() + 1;
		else
			return heightRange.min() - placement.size().getY() + 2;
	}
}