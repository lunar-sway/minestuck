package com.mraof.minestuck.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.TemplatePlacement;
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
 * Structure nbt defined tree stumps intended for Forest Lands
 */
public class TreeStumpFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation TREE_STUMP = new ResourceLocation(Minestuck.MOD_ID, "tree_stump");
	
	public TreeStumpFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		StructureTemplate template = level.getLevel().getStructureManager().getOrCreate(TREE_STUMP);
		BlockPos centerPos = context.origin();
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, centerPos, rand);
		
		if(placement.heightRange(Heightmap.Types.OCEAN_FLOOR, context.level()).difference() > 2)
			return false;
		
		int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, centerPos.getX(), centerPos.getZ()) - rand.nextInt(1) - 4;
		placement.placeWithStructureBlockRegistry(y, context);
		
		return true;
	}
	
}