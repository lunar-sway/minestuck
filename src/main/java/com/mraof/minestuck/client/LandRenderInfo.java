package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.renderer.LandSkyRenderer;
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
		return biomeFogColor;
	}
	
	@Override
	public boolean isFoggyAt(int x, int z)
	{
		return false;
	}
}
