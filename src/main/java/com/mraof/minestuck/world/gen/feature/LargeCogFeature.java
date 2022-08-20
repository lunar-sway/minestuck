package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class LargeCogFeature extends AbstractTemplateFeature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_LARGE_COG_1 = new ResourceLocation(Minestuck.MOD_ID, "large_cog_1");
	private static final ResourceLocation STRUCTURE_LARGE_COG_2 = new ResourceLocation(Minestuck.MOD_ID, "large_cog_2");
	
	public LargeCogFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public ResourceLocation pickTemplate(Random random)
	{
		return random.nextBoolean() ? STRUCTURE_LARGE_COG_1 : STRUCTURE_LARGE_COG_2;
	}
	
	@Override
	protected int pickY(WorldGenLevel level, BlockPos pos, Vec3i templateSize, Random random)
	{
		return Math.max(0, minWorldHeightInSize(level, pos, templateSize) - random.nextInt(4));
	}
}