package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderShadow extends Render
{

	public RenderShadow(float shadowSize) 
	{
		this.shadowSize = shadowSize;
	}
	public void doRenderShadow(Entity entity, double d0, double d1, double d2, float f, float f1)
	{
		this.shadowSize = entity.width;
	}
	@Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) 
	{
		this.doRenderShadow(entity, d0, d1, d2, f, f1);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}
	
}
