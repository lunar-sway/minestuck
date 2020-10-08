package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;

public class LandProperties
{
	public float skylightBase;
	private Vector3d skyColor;
	private Vector3d fogColor;
	
	public Biome.Category category = Biome.Category.NONE;
	public Biome.RainType rainType = Biome.RainType.NONE;
	public ForceType forceRain = ForceType.OFF, forceThunder = ForceType.OFF;	//TODO Make private and set up setters that'd prevent combinations that doesn't make sense, + also setting forceRain + rainType at the same time
	public float temperature = 0.7F, downfall = 0.5F;
	public float normalBiomeDepth = MSBiomes.LAND_NORMAL.getDepth(), normalBiomeScale = MSBiomes.LAND_NORMAL.getScale();
	public float roughBiomeDepth = MSBiomes.LAND_ROUGH.getDepth(), roughBiomeScale = MSBiomes.LAND_ROUGH.getScale();
	public float oceanBiomeDepth = MSBiomes.LAND_OCEAN.getDepth(), oceanBiomeScale = MSBiomes.LAND_OCEAN.getScale();
	
	public LandProperties(TerrainLandType landType)
	{
		skylightBase = landType.getSkylightBase();
		skyColor = landType.getSkyColor();
		fogColor = landType.getFogColor();
	}
	
	public void load(LandTypePair types)
	{
		types.terrain.setProperties(this);
		types.title.setProperties(this);
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