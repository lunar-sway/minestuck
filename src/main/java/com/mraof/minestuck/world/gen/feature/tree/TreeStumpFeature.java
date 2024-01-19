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
	protected int pickY(WorldGenLevel level, BlockPos pos, Vec3i templateSize, RandomSource random)
	{
		//same as minWorldHeightInSize() but using ocean floor heightmap
		int minY = Integer.MAX_VALUE;
		for(BlockPos floorPos : BlockPos.betweenClosed(pos, pos.offset(templateSize.getX(), 0, templateSize.getZ())))
			minY = Math.min(minY, level.getHeight(Heightmap.Types.OCEAN_FLOOR, floorPos.getX(), floorPos.getZ()));
		
		return minY;
	}
}