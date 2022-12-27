package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SmallCogFeature extends AbstractTemplateFeature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_SMALL_COG = new ResourceLocation(Minestuck.MOD_ID, "small_cog");
	
	public SmallCogFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public ResourceLocation pickTemplate(RandomSource random)
	{
		return STRUCTURE_SMALL_COG;
	}
	
	@Override
	protected int pickY(WorldGenLevel level, BlockPos pos, Vec3i templateSize, RandomSource random)
	{
		return Math.max(0, minWorldHeightInSize(level, pos, templateSize) - random.nextInt(3));
	}
}