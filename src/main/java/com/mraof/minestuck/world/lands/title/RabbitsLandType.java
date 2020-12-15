package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;

public class RabbitsLandType extends TitleLandType
{
	public static final String RABBITS = "minestuck.rabbits";
	public static final String BUNNIES = "minestuck.bunnies";
	
	public RabbitsLandType()
	{
		super(EnumAspect.LIFE);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {RABBITS, BUNNIES};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.PINK_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.LIGHT_GRAY_CARPET.getDefaultState());
	}
	/*
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.type == BiomeType.NORMAL)
			biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, MSFeatures.RABBIT_PLACEMENT.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_CHANCE_HEIGHTMAP.configure(new HeightWithChanceConfig(6, 0.2F))));
		if(biome.type == BiomeType.ROUGH)
			biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, MSFeatures.RABBIT_PLACEMENT.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_CHANCE_HEIGHTMAP.configure(new HeightWithChanceConfig(3, 0.2F))));
	}
	*/
	@Override
	public boolean isAspectCompatible(TerrainLandType aspect)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		aspect.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;
	}
}