package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * A version of the {@link net.minecraft.world.level.levelgen.feature.FossilFeature}, but it can be spawned on the terrain surface reliably.
 */
public class SurfaceFossilsFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_SPINE_01 = new ResourceLocation("fossil/spine_1");
	private static final ResourceLocation STRUCTURE_SPINE_02 = new ResourceLocation("fossil/spine_2");
	private static final ResourceLocation STRUCTURE_SPINE_03 = new ResourceLocation("fossil/spine_3");
	private static final ResourceLocation STRUCTURE_SPINE_04 = new ResourceLocation("fossil/spine_4");
	private static final ResourceLocation STRUCTURE_SKULL_01 = new ResourceLocation("fossil/skull_1");
	private static final ResourceLocation STRUCTURE_SKULL_02 = new ResourceLocation("fossil/skull_2");
	private static final ResourceLocation STRUCTURE_SKULL_03 = new ResourceLocation("fossil/skull_3");
	private static final ResourceLocation STRUCTURE_SKULL_04 = new ResourceLocation("fossil/skull_4");
	private static final ResourceLocation[] FOSSILS = new ResourceLocation[]{STRUCTURE_SPINE_01, STRUCTURE_SPINE_02, STRUCTURE_SPINE_03, STRUCTURE_SPINE_04, STRUCTURE_SKULL_01, STRUCTURE_SKULL_02, STRUCTURE_SKULL_03, STRUCTURE_SKULL_04};
	
	public SurfaceFossilsFeature(Codec<NoneFeatureConfiguration> codec)
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
		RandomSource random = context.random();
		ResourceLocation templateId = FOSSILS[random.nextInt(FOSSILS.length)];
		StructureTemplate template = level.getLevel().getStructureManager().getOrCreate(templateId);
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, context.origin(), random);
		
		int y = this.yForPlacement(level, placement);
		placement.placeWithStructureBlockRegistryAt(y, context);
		
		return true;
	}
}
