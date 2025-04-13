package com.mraof.minestuck.client;

import com.mraof.minestuck.client.renderer.VeilSkyRenderer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VeilSpecialEffects extends DimensionSpecialEffects
{
	public VeilSpecialEffects()
	{
		super(Float.NaN, false, SkyType.NONE, true, false);
	}
	
	@Override
	public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f modelViewMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog)
	{
		VeilSkyRenderer.render(modelViewMatrix, level);
		return true;
	}
	
	@Override
	public Vec3 getBrightnessDependentFogColor(Vec3 biomeFogColor, float brightness)
	{
		return new Vec3(0D, 0D, 0D);
	}
	
	@Nullable
	@Override
	public float[] getSunriseColor(float timeOfDay, float partialTicks)
	{
		return null;
	}
	
	@Override
	public boolean isFoggyAt(int x, int z)
	{
		return false;
	}
}
