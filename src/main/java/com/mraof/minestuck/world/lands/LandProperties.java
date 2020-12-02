package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

public class LandProperties
{
	public final LandBiomeSet biomes;
	public float skylightBase;
	private Vec3d skyColor;
	private Vec3d fogColor;
	
	public Biome.Category category = Biome.Category.NONE;
	public ForceType forceRain = ForceType.OFF, forceThunder = ForceType.OFF;	//TODO Make private and set up setters that'd prevent combinations that doesn't make sense, + also setting forceRain + rainType at the same time
	public float normalBiomeDepth = MSBiomes.DEFAULT_LAND.NORMAL.get().getDepth(), normalBiomeScale = MSBiomes.DEFAULT_LAND.NORMAL.get().getScale();
	public float roughBiomeDepth = MSBiomes.DEFAULT_LAND.ROUGH.get().getDepth(), roughBiomeScale = MSBiomes.DEFAULT_LAND.ROUGH.get().getScale();
	public float oceanBiomeDepth = MSBiomes.DEFAULT_LAND.OCEAN.get().getDepth(), oceanBiomeScale = MSBiomes.DEFAULT_LAND.OCEAN.get().getScale();
	
	public LandProperties(TerrainLandType landType)
	{
		biomes = landType.getBiomeSet();
		skylightBase = landType.getSkylightBase();
		skyColor = landType.getSkyColor();
		fogColor = landType.getFogColor();
	}
	
	public void load(LandTypePair types)
	{
		types.terrain.setProperties(this);
		types.title.setProperties(this);
	}
	
	public void mergeFogColor(Vec3d fogColor, float strength)
	{
		double d1 = (this.fogColor.x + fogColor.x*strength)/(1 + strength);
		double d2 = (this.fogColor.y + fogColor.y*strength)/(1 + strength);
		double d3 = (this.fogColor.z + fogColor.z*strength)/(1 + strength);
		this.fogColor = new Vec3d(d1, d2, d3);
	}
	
	public Vec3d getSkyColor()
	{
		return skyColor;
	}
	
	public Vec3d getFogColor()
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