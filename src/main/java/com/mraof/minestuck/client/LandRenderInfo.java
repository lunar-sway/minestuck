package com.mraof.minestuck.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.client.renderer.LandSkyRenderer;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LandRenderInfo extends DimensionSpecialEffects
{
	public LandRenderInfo()
	{
		super(128.0F, true, DimensionSpecialEffects.SkyType.NORMAL, false, false);
	}
	
	@Override
	public boolean renderSky(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog)
	{
		LandSkyRenderer.render(ticks, partialTick, poseStack, level, Minecraft.getInstance());
		return true;
	}
	
	@Override
	public Vec3 getBrightnessDependentFogColor(Vec3 biomeFogColor, float brightness)
	{
		LandProperties properties = ClientDimensionData.getProperties(Minecraft.getInstance().level);
		return properties != null ? properties.getFogColor() : biomeFogColor;
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
