package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ShadowRenderer<T extends Entity> extends EntityRenderer<T>
{
	public ShadowRenderer(EntityRendererManager manager, float shadowSize)
	{
		super(manager);
		this.shadowRadius = shadowSize;
	}
	
	public void doRenderShadow(T entity)
	{
		this.shadowRadius = entity.getBbWidth();
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		this.doRenderShadow(entityIn);
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity)
	{
		return null;
	}
}