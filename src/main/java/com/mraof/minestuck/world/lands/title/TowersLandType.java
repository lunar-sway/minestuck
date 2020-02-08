package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BushConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class TowersLandType extends TitleLandType
{
	public static final String TOWERS = "minestuck.towers";
	
	public TowersLandType()
	{
		super(EnumAspect.HOPE);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {TOWERS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.LIGHT_BLUE_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.YELLOW_CARPET.getDefaultState());
	}
	
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blockRegistry)
	{
		if(biome.staticBiome != MSBiomes.LAND_OCEAN)
		{
			biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(MSFeatures.TOWER, IFeatureConfig.NO_FEATURE_CONFIG, Placement.CHANCE_PASSTHROUGH, new ChanceConfig(20)));
		}
		
		if(biome.staticBiome == MSBiomes.LAND_ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(MSFeatures.LARGE_PILLAR, new BushConfig(blockRegistry.getBlockState("structure_primary")), Placement.TOP_SOLID_HEIGHTMAP, IPlacementConfig.NO_PLACEMENT_CONFIG));
		}
	}
}