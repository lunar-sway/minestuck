package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class SulfurPoolFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation SULFUR_POOL = new ResourceLocation(Minestuck.MOD_ID, "sulfur_pool");
	private static final ResourceLocation LARGE_SULFUR_POOL = new ResourceLocation(Minestuck.MOD_ID, "large_sulfur_pool");
	
	public SulfurPoolFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	private static ResourceLocation pickTemplate(RandomSource random)
	{
		return random.nextBoolean() ? SULFUR_POOL: LARGE_SULFUR_POOL;
	}
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		StructureTemplate template = context.level().getLevel().getStructureManager().getOrCreate(pickTemplate(rand));
		BlockPos centerPos = context.origin();
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, centerPos, rand);
		if(placement.heightRange(Heightmap.Types.OCEAN_FLOOR, context.level()).difference() > 3)
			return false;
		
		int y = placement.minHeight(Heightmap.Types.WORLD_SURFACE_WG, level);
		placement.placeAt(y, context);
		
		return true;
	}
}