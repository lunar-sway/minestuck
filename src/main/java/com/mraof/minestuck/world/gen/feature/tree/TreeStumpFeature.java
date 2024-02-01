package com.mraof.minestuck.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.AbstractTemplateFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Structure nbt defined tree stumps intended for Forest Lands
 */
public class TreeStumpFeature extends AbstractTemplateFeature<NoneFeatureConfiguration>
{
	private static final ResourceLocation TREE_STUMP = new ResourceLocation(Minestuck.MOD_ID, "tree_stump");
	
	public TreeStumpFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	protected ResourceLocation pickTemplate(RandomSource random)
	{
		return TREE_STUMP;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		//continue with normal placement only if the terrain is relatively even
		if(isEvenTerrain(context))
			return super.place(context);
		else
			return false;
	}
	
	private boolean isEvenTerrain(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		
		for(BlockPos floorPos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 0, 4)))
		{
			int posHeight = level.getHeight(Heightmap.Types.OCEAN_FLOOR, floorPos.getX(), floorPos.getZ());
			minY = Math.min(minY, posHeight);
			maxY = Math.max(maxY, posHeight);
		}
		
		//only considered even terrain for placement if theres a difference of 2 blocks or less between tallest and shortest points
		return maxY - minY <= 2;
	}
	
	@Override
	protected int pickY(WorldGenLevel level, BlockPos pos, Vec3i templateSize, RandomSource random)
	{
		return level.getHeight(Heightmap.Types.OCEAN_FLOOR, pos.getX(), pos.getZ()) - random.nextInt(1) - 4;
	}
}