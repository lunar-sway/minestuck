package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.world.phys.Vec3;

public class LandProperties
{
	public final LandBiomeSetType biomes;
	public float skylightBase;
	private Vec3 skyColor;
	private Vec3 fogColor;
	
	public ForceType forceRain = ForceType.OFF, forceThunder = ForceType.OFF;	//TODO Make private and set up setters that'd prevent combinations that doesn't make sense, + also setting forceRain + rainType at the same time
	
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
	
	public void mergeFogColor(Vec3 fogColor, float strength)
	{
		double d1 = (this.fogColor.x + fogColor.x*strength)/(1 + strength);
		double d2 = (this.fogColor.y + fogColor.y*strength)/(1 + strength);
		double d3 = (this.fogColor.z + fogColor.z*strength)/(1 + strength);
		this.fogColor = new Vec3(d1, d2, d3);
	}
	
	public Vec3 getSkyColor()
	{
		return skyColor;
	}
	
	public Vec3 getFogColor()
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