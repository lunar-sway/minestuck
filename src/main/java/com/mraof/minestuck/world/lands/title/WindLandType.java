package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;

public class WindLandType extends TitleLandType
{
	public static final String WIND = "minestuck.wind";
	
	public WindLandType()
	{
		super(EnumAspect.BREATH);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {WIND};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.LIGHT_BLUE_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.CYAN_CARPET.getDefaultState());
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.mergeFogColor(new Vec3d(0.1, 0.2, 0.8), 0.3F);
		if(properties.rainType == Biome.RainType.NONE)
			properties.rainType = Biome.RainType.RAIN;
		if(properties.forceRain == LandProperties.ForceType.OFF)
			properties.forceRain = LandProperties.ForceType.DEFAULT;
		
		properties.normalBiomeScale *= 0.6;
		properties.roughBiomeScale *= 0.6;
		properties.roughBiomeDepth = (properties.roughBiomeDepth + properties.normalBiomeDepth)/2;
	}
	
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.ROCK_SPIKE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(50))));
	}
}