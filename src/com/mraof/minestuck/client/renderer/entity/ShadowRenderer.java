package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ShadowRenderer<T extends Entity> extends EntityRenderer<T>
{
	public ShadowRenderer(EntityRendererManager manager, float shadowSize)
	{
		super(manager);
		this.shadowSize = shadowSize;
	}
	
	public void doRenderShadow(T entity, double d0, double d1, double d2, float f, float f1)
	{
		this.shadowSize = entity.getWidth();
	}
	
	@Override
	public void doRender(T entity, double d0, double d1, double d2, float f, float f1)
	{
		this.doRenderShadow(entity, d0, d1, d2, f, f1);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(T entity)
	{
		return null;
	}
}