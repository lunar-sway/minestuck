package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.world.biome.BiomeType;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;

public class ClockworkLandType extends TitleLandType
{
	public static final String CLOCKWORK = "minestuck.clockwork";
	public static final String GEARS = "minestuck.gears";
	
	public ClockworkLandType()
	{
		super(EnumAspect.TIME);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{CLOCKWORK, GEARS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.LIGHT_GRAY_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.RED_CARPET.getDefaultState());
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.mergeFogColor(new Vec3d(0.5, 0.5, 0.5), 0.5F);
	}
	
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.type == BiomeType.ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.COG.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_PASSTHROUGH.configure(new ChanceConfig(2))));
		} else
		{
			biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.COG.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_PASSTHROUGH.configure(new ChanceConfig(10))));
		}
		
		if(biome.type == BiomeType.OCEAN)
		{
			biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.FLOOR_COG.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_PASSTHROUGH.configure(new ChanceConfig(3))));
		} else
		{
			biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.FLOOR_COG.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_PASSTHROUGH.configure(new ChanceConfig(20))));
		}
	}
}