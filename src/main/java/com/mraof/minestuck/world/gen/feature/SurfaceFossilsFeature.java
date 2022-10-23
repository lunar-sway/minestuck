package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

/**
 * A version of the {@link net.minecraft.world.level.levelgen.feature.FossilFeature}, but it can be spawned on the terrain surface reliably.
 */
public class SurfaceFossilsFeature extends AbstractTemplateFeature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_SPINE_01 = new ResourceLocation("fossil/spine_1");
	private static final ResourceLocation STRUCTURE_SPINE_02 = new ResourceLocation("fossil/spine_2");
	private static final ResourceLocation STRUCTURE_SPINE_03 = new ResourceLocation("fossil/spine_3");
	private static final ResourceLocation STRUCTURE_SPINE_04 = new ResourceLocation("fossil/spine_4");
	private static final ResourceLocation STRUCTURE_SKULL_01 = new ResourceLocation("fossil/skull_1");
	private static final ResourceLocation STRUCTURE_SKULL_02 = new ResourceLocation("fossil/skull_2");
	private static final ResourceLocation STRUCTURE_SKULL_03 = new ResourceLocation("fossil/skull_3");
	private static final ResourceLocation STRUCTURE_SKULL_04 = new ResourceLocation("fossil/skull_4");
	private static final ResourceLocation[] FOSSILS = new ResourceLocation[]{STRUCTURE_SPINE_01, STRUCTURE_SPINE_02, STRUCTURE_SPINE_03, STRUCTURE_SPINE_04, STRUCTURE_SKULL_01, STRUCTURE_SKULL_02, STRUCTURE_SKULL_03, STRUCTURE_SKULL_04};
	
	public SurfaceFossilsFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	protected ResourceLocation pickTemplate(Random random)
	{
		return FOSSILS[random.nextInt(FOSSILS.length)];
	}
	
	@Override
	protected int pickY(WorldGenLevel level, BlockPos pos, Vec3i templateSize, Random random)
	{
		int yMin = level.getMaxBuildHeight(), yMax = level.getMinBuildHeight();
		for(BlockPos floorPos : BlockPos.betweenClosed(0, 0, 0, templateSize.getX(), 0, templateSize.getZ()))
		{
			int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX() + floorPos.getX(), pos.getZ() + floorPos.getZ());
			yMax = Math.max(yMax, y);
			yMin = Math.min(yMin, y);
		}
		
		if(yMin == yMax)
			return yMin - templateSize.getY() + 1;
		else
			return yMin - templateSize.getY() + 2;
	}
}
