package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ShadowRenderer<T extends Entity> extends EntityRenderer<T>
{
	public ShadowRenderer(EntityRendererProvider.Context context, float shadowSize)
	{
		super(context);
		this.shadowRadius = shadowSize;
	}
	
	public void doRenderShadow(T entity)
	{
		this.shadowRadius = entity.getBbWidth();
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		this.doRenderShadow(entityIn);
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity)
	{
		return null;
	}
}