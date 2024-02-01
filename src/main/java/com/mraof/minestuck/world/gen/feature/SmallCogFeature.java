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

public class SmallCogFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_SMALL_COG = new ResourceLocation(Minestuck.MOD_ID, "small_cog");
	
	public SmallCogFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		StructureTemplate template = level.getLevel().getStructureManager().getOrCreate(STRUCTURE_SMALL_COG);
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, context.origin(), rand);
		
		int y = Math.max(0, placement.minHeight(Heightmap.Types.WORLD_SURFACE_WG, level) - rand.nextInt(3));
		placement.placeWithStructureBlockRegistry(y, context);
		
		return true;
	}
}