package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.biome.LandBiome;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;

public class LandProperties
{
	public final LandBiomeSet biomes;
	public float skylightBase;
	private Vector3d skyColor;
	private Vector3d fogColor;
	
	public Biome.Category category = Biome.Category.NONE;
	public ForceType forceRain = ForceType.OFF, forceThunder = ForceType.OFF;	//TODO Make private and set up setters that'd prevent combinations that doesn't make sense, + also setting forceRain + rainType at the same time
	public float normalBiomeDepth = LandBiome.DEFAULT_NORMAL_DEPTH, normalBiomeScale = LandBiome.DEFAULT_NORMAL_SCALE;
	public float roughBiomeDepth = LandBiome.DEFAULT_ROUGH_DEPTH, roughBiomeScale = LandBiome.DEFAULT_ROUGH_SCALE;
	public float oceanBiomeDepth = LandBiome.DEFAULT_OCEAN_DEPTH, oceanBiomeScale = LandBiome.DEFAULT_OCEAN_SCALE;
	
	public static LandProperties create(LandTypePair types)
	{
		LandProperties properties = createPartial(types.getTerrain());
		types.getTitle().setProperties(properties);
		
		return properties;
	}
	
	public static LandProperties createPartial(TerrainLandType type)
	{
		LandProperties properties = new LandProperties(type);
		type.setProperties(properties);
		
		return properties;
	}
	
	private LandProperties(TerrainLandType type)
	{
		biomes = type.getBiomeSet();
		skylightBase = type.getSkylightBase();
		skyColor = type.getSkyColor();
		fogColor = type.getFogColor();
		
	}
	
	public void mergeFogColor(Vector3d fogColor, float strength)
	{
		double d1 = (this.fogColor.x + fogColor.x*strength)/(1 + strength);
		double d2 = (this.fogColor.y + fogColor.y*strength)/(1 + strength);
		double d3 = (this.fogColor.z + fogColor.z*strength)/(1 + strength);
		this.fogColor = new Vector3d(d1, d2, d3);
	}
	
	public Vector3d getSkyColor()
	{
		return skyColor;
	}
	
	public Vector3d getFogColor()
	{
		return fogColor;
	}
	
	public enum ForceType
	{
		ON,
		DEFAULT,
		OFF
	}
}