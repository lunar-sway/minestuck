package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public abstract class AbstractTemplateFeature<T extends FeatureConfiguration> extends Feature<T>
{
	public AbstractTemplateFeature(Codec<T> pCodec)
	{
		super(pCodec);
	}
	
	protected abstract ResourceLocation pickTemplate(RandomSource random);
	
	protected abstract int pickY(WorldGenLevel level, TemplatePlacement placement, RandomSource random);
	
	protected static int minWorldHeightInSize(WorldGenLevel level, TemplatePlacement placement)
	{
		int minY = Integer.MAX_VALUE;
		for(BlockPos floorPos : placement.xzPlacedRange())
			minY = Math.min(minY, level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, floorPos.getX(), floorPos.getZ()));
		
		return minY;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<T> context)
	{
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		StructureTemplate template = level.getLevel().getStructureManager().getOrCreate(this.pickTemplate(rand));
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, context.origin(), rand);
		
		int y = this.pickY(level, placement, rand);
		placement.placeWithStructureBlockRegistry(y, context);
		
		return true;
	}
	
}
