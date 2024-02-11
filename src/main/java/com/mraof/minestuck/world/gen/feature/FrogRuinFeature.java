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

/**
 * Structure nbt defined dilapidated frog ruins for Frog Lands
 */
public class FrogRuinFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation FROG_RUINS_0 = new ResourceLocation(Minestuck.MOD_ID, "frog_ruins_0");
	private static final ResourceLocation FROG_RUINS_1 = new ResourceLocation(Minestuck.MOD_ID, "frog_ruins_1");
	
	public FrogRuinFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	private static ResourceLocation pickTemplate(RandomSource random)
	{
		return random.nextBoolean() ? FROG_RUINS_0: FROG_RUINS_1;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		StructureTemplate template = level.getLevel().getStructureManager().getOrCreate(pickTemplate(rand));
		BlockPos centerPos = context.origin();
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, centerPos, rand);
		
		if(placement.heightRange(Heightmap.Types.OCEAN_FLOOR, context.level()).difference() > 3)
			return false;
		
		int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, centerPos.getX(), centerPos.getZ()) - rand.nextInt(1) - 3;
		placement.placeWithStructureBlockRegistryAt(y, context);
		
		return true;
	}
}