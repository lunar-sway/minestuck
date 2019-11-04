package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.lands.LandAspects;
import com.mraof.minestuck.world.LandDimension;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

public class ThunderLandAspect extends TitleLandAspect
{
	public static final String THUNDER = "minestuck.thunder";
	public static final String LIGHTNING = "minestuck.lightning";
	public static final String STORMS = "minestuck.storms";
	
	public ThunderLandAspect()
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
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.mergeFogColor(new Vec3d(0.1, 0.1, 0.2), 0.5F);
	}
	
	@Override
	public void setBiomeSettings(LandBiomeHolder settings)
	{
		settings.rainType = Biome.RainType.RAIN; //TODO Add feature to make an eternal thunderstorm
		settings.downfall += 0.1F;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = Math.min(Math.max(0.5F, settings.oceanChance), settings.oceanChance*1.2F);	//Increase ocean chance by a factor 1.2, but not higher than to 0.5F
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		LandBiomeHolder biomeSettings = new LandBiomeHolder(new LandAspects(aspect, this), true);
		return biomeSettings.rainType != Biome.RainType.SNOW;
	}
}