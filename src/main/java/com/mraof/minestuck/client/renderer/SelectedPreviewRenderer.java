package com.mraof.minestuck.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;

public final class SelectedPreviewRenderer implements VertexConsumer
{
	private final VertexConsumer delegate;
	private final float alphaMultiplier;
	
	public SelectedPreviewRenderer(VertexConsumer delegate, float alphaMultiplier)
	{
		this.delegate = delegate;
		this.alphaMultiplier = Mth.clamp(alphaMultiplier, 0f, 1f);
	}
	
	@Override
	public VertexConsumer addVertex(float x, float y, float z)
	{
		return delegate.addVertex(x, y, z);
	}
	
	@Override
	public VertexConsumer setColor(int red, int green, int blue, int alpha)
	{
		return delegate.setColor(red, green, blue, Math.round(alpha * alphaMultiplier));
	}
	
	@Override
	public VertexConsumer setUv(float u, float v)
	{
		return delegate.setUv(u, v);
	}
	
	@Override
	public VertexConsumer setUv1(int u, int v)
	{
		return delegate.setUv1(u, v);
	}
	
	@Override
	public VertexConsumer setUv2(int u, int v)
	{
		return delegate.setUv2(u, v);
	}
	
	@Override
	public VertexConsumer setNormal(float x, float y, float z)
	{
		return delegate.setNormal(x, y, z);
	}
}
