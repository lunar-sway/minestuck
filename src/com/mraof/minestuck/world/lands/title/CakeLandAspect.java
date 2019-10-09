package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;

public class CakeLandAspect extends TitleLandAspect
{
	public CakeLandAspect()
	{
		super(EnumAspect.HEART);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"cake", "dessert"};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.ORANGE_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.MAGENTA_CARPET.getDefaultState());
	}
	
	@Override
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.staticBiome != MSBiomes.LAND_OCEAN)
		{
			biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(MSFeatures.CAKE_PEDESTAL, IFeatureConfig.NO_FEATURE_CONFIG, Placement.CHANCE_PASSTHROUGH, new ChanceConfig(100)));
		}
		
		biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(MSFeatures.CAKE, new ProbabilityConfig(biome.getDefaultTemperature()/2), Placement.TOP_SOLID_HEIGHTMAP_RANGE, new TopSolidRangeConfig(0, 5)));
	}
}