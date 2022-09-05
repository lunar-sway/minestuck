package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

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
		registry.setBlockState("structure_wool_2", Blocks.BLUE_WOOL.defaultBlockState());
		registry.setBlockState("carpet", Blocks.GREEN_CARPET.defaultBlockState());
		
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.mergeFogColor(new Vec3(0.1, 0.1, 0.2), 0.5F);
		properties.forceRain = LandProperties.ForceType.ON;
		properties.forceThunder = LandProperties.ForceType.ON;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		if(settings.hasOceanTerrain())
			settings.oceanThreshold = Math.min(Math.max(0, settings.oceanThreshold), settings.oceanThreshold + 0.1F);	//Increase ocean chance by a factor 1.2, but not higher than to 0.5F
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandType otherType)
	{
		LandProperties properties = LandProperties.createPartial(otherType);
		
		return properties.biomes.getPrecipitation() == Biome.Precipitation.RAIN;
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_THUNDER;
	}
}