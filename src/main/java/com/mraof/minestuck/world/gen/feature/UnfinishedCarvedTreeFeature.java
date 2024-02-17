package com.mraof.minestuck.world.gen.feature;

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

public class UnfinishedCarvedTreeFeature extends AbstractTemplateFeature<NoneFeatureConfiguration>
{
	private static final ResourceLocation UNFINISHED_CARVED_TREE = new ResourceLocation(Minestuck.MOD_ID, "unfinished_carved_tree");
	
	public UnfinishedCarvedTreeFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	protected ResourceLocation pickTemplate(RandomSource random)
	{
		return UNFINISHED_CARVED_TREE;
	}
	
	@Override
	protected int pickY(WorldGenLevel level, BlockPos pos, Vec3i templateSize, RandomSource random)
	{
		return minWorldHeightInSize(level, pos, templateSize);
	}
}