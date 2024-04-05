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

public class CastIronBuildingFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation SMALL_CAST_FRAME = new ResourceLocation(Minestuck.MOD_ID, "small_cast_frame");
	private static final ResourceLocation TALL_CAST_FRAME = new ResourceLocation(Minestuck.MOD_ID, "tall_cast_frame");
	
	public CastIronBuildingFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	private static ResourceLocation pickTemplate(RandomSource random)
	{
		return random.nextBoolean() ? SMALL_CAST_FRAME: TALL_CAST_FRAME ;
	}
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		StructureTemplate template = context.level().getLevel().getStructureManager().getOrCreate(pickTemplate(rand));
		BlockPos centerPos = context.origin();
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, centerPos, rand);
		
		int y = placement.minHeight(Heightmap.Types.WORLD_SURFACE_WG, level);
		placement.placeAt(y, context);
		
		return true;
	}
}