package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.util.SoundEvent;

public class CakeLandType extends TitleLandType
{
	public static final String CAKE = "minestuck.cake";
	public static final String DESSERTS = "minestuck.desserts";
	
	public CakeLandType()
	{
		super(EnumAspect.HEART);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {CAKE, DESSERTS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.ORANGE_WOOL.defaultBlockState());
		registry.setBlockState("carpet", Blocks.MAGENTA_CARPET.defaultBlockState());
	}
	/*
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.type != BiomeType.OCEAN)
		{
			biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.CAKE_PEDESTAL.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_PASSTHROUGH.configure(new ChanceConfig(100))));
		}

		biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.CAKE.withConfiguration(new ProbabilityConfig(biome.getDefaultTemperature()/2)).withPlacement(Placement.TOP_SOLID_HEIGHTMAP_RANGE.configure(new TopSolidRangeConfig(0, 5))));
	}
	 */
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_CAKE;
	}
}