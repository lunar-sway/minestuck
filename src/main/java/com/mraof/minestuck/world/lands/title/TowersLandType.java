package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.world.biome.BiomeType;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
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
		if(biome.type != BiomeType.OCEAN)
		{
			biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.TOWER.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_PASSTHROUGH.configure(new ChanceConfig(20))));
		}
		
		if(biome.type == BiomeType.ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.LARGE_PILLAR.withConfiguration(new BlockStateFeatureConfig(blockRegistry.getBlockState("structure_primary"))).withPlacement(Placement.TOP_SOLID_HEIGHTMAP.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		}
	}
}