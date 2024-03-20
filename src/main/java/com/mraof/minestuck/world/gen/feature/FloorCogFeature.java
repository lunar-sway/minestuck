package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class FloorCogFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_LARGE_FLOOR_COG_1 = new ResourceLocation(Minestuck.MOD_ID, "large_floor_cog_1");
	private static final ResourceLocation STRUCTURE_LARGE_FLOOR_COG_2 = new ResourceLocation(Minestuck.MOD_ID, "large_floor_cog_2");
	
	public FloorCogFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	private int yForPlacement(WorldGenLevel level, TemplatePlacement placement)
	{
		TemplatePlacement.Range heightRange = placement.heightRange(Heightmap.Types.WORLD_SURFACE_WG, level);
		
		if(heightRange.min() == heightRange.max())
			return heightRange.min() - placement.size().getY() + 1;
		else
			return heightRange.min() - placement.size().getY() + 2;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		ResourceLocation templateId = rand.nextBoolean() ? STRUCTURE_LARGE_FLOOR_COG_1 : STRUCTURE_LARGE_FLOOR_COG_2;
		StructureTemplate template = level.getLevel().getStructureManager().getOrCreate(templateId);
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, context.origin(), rand);
		
		int y = this.yForPlacement(level, placement);
		placement.placeWithStructureBlockRegistryAt(y, context);
		
		return true;
	}
}