package com.mraof.minestuck.client;

import com.mraof.minestuck.client.renderer.LandSkyRenderer;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.math.vector.Vector3d;

public class LandRenderInfo extends DimensionRenderInfo
{
	public LandRenderInfo()
	{
		super(128.0F, true, DimensionRenderInfo.FogType.NORMAL, false, false);
		setSkyRenderHandler(new LandSkyRenderer());
	}
	
	@Override
	public Vector3d getBrightnessDependentFogColor(Vector3d biomeFogColor, float brightness)
	{
		LandProperties properties = ClientDimensionData.getProperties(Minecraft.getInstance().level);
		return properties != null ? properties.getFogColor() : biomeFogColor;
	}
	
	@Override
	public boolean isFoggyAt(int x, int z)
	{
		return false;
	}
}
