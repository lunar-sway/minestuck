package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;

public class ThunderLandType extends TitleLandType
{
	public static final String THUNDER = "minestuck.thunder";
	public static final String LIGHTNING = "minestuck.lightning";
	public static final String STORMS = "minestuck.storms";
	
	public ThunderLandType()
	{
		super(EnumAspect.DOOM);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{THUNDER, LIGHTNING, STORMS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.BLUE_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.GREEN_CARPET.getDefaultState());
		
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.mergeFogColor(new Vector3d(0.1, 0.1, 0.2), 0.5F);
		properties.rainType = Biome.RainType.RAIN;
		properties.forceRain = LandProperties.ForceType.ON;
		properties.forceThunder = LandProperties.ForceType.ON;
		properties.downfall += 0.1F;
	}
	/*
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = Math.min(Math.max(0.5F, settings.oceanChance), settings.oceanChance*1.2F);	//Increase ocean chance by a factor 1.2, but not higher than to 0.5F
	}
	*/
	@Override
	public boolean isAspectCompatible(TerrainLandType aspect)
	{
		LandProperties properties = new LandProperties(aspect);
		aspect.setProperties(properties);
		return properties.rainType != Biome.RainType.SNOW;
	}
}