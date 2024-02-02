package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Structure nbt defined dilapidated frog ruins for Frog Lands
 */
public class FrogRuinFeature extends AbstractTemplateFeature<NoneFeatureConfiguration>
{
	private static final ResourceLocation FROG_RUINS_0 = new ResourceLocation(Minestuck.MOD_ID, "frog_ruins_0");
	private static final ResourceLocation FROG_RUINS_1 = new ResourceLocation(Minestuck.MOD_ID, "frog_ruins_1");
	
	public FrogRuinFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	protected ResourceLocation pickTemplate(RandomSource random)
	{
		return random.nextBoolean() ? FROG_RUINS_0: FROG_RUINS_1;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		//continue with normal placement only if the terrain is relatively even
		if(isEvenTerrain(context, context.origin().offset(8, 0, 8), 3))
			return super.place(context);
		else
			return false;
	}
	
	@Override
	protected int pickY(WorldGenLevel level, BlockPos pos, Vec3i templateSize, RandomSource random)
	{
		return level.getHeight(Heightmap.Types.OCEAN_FLOOR, pos.getX(), pos.getZ()) - random.nextInt(1) - 3;
	}
}