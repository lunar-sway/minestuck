package com.mraof.minestuck.world.gen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

public class SmallLibraryFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_SMALL_LIBRARY = new ResourceLocation(Minestuck.MOD_ID, "small_library");
	
	public SmallLibraryFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		StructureTemplateManager templates = level.getLevel().getStructureManager();
		StructureTemplate template = templates.getOrCreate(STRUCTURE_SMALL_LIBRARY);
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, context.origin(), rand);
		
		StructurePlaceSettings settings = new StructurePlaceSettings();
		if(rand.nextBoolean())
		{	//Replace 20% of bookcases with air
			settings.addProcessor(new RuleProcessor(ImmutableList.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.BOOKSHELF, 0.2F), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()))));
		}
		
		int centerX = template.getSize().getX() / 2;
		int endZ = template.getSize().getZ() - 1;
		int door1Height = TemplatePlacement.maxHeight(Heightmap.Types.OCEAN_FLOOR_WG, level, placement.xzRange(centerX - 1, 0, centerX + 1, 0));
		int door2Height = TemplatePlacement.maxHeight(Heightmap.Types.OCEAN_FLOOR_WG, level, placement.xzRange(centerX - 1, endZ, centerX + 1, endZ));
		
		placement.placeWithStructureBlockRegistryAt(Math.min(door1Height, door2Height) - 1, context, settings);
		
		return true;
	}
}
