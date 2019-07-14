package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.item.VitalityGelEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderVitalityGel extends Render<VitalityGelEntity>
{
	
	public RenderVitalityGel(RenderManager manager)
	{
		super(manager);
		this.shadowSize = 0.15F;
		this.shadowOpaque = .75F;
	}
	
	@Override
	public void doRender(VitalityGelEntity entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translatef((float)x, (float)y + entity.getSizeByValue()/2, (float)z);
		this.bindEntityTexture(entity);
		BufferBuilder vertexbuffer = Tessellator.getInstance().getBuffer();
		int j = entity.getBrightnessForRender();
		int k = j % 65536;
		int l = j / 65536;
		OpenGlHelper.glMultiTexCoord2f(OpenGlHelper.GL_TEXTURE1, (float)k, (float)l);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		
		float size = entity.getSizeByValue();
		GlStateManager.scalef(size, size, size);
		float scaleFactor = MathHelper.sin((entity.age + partialTicks)/ 10f + entity.animationOffset);
		GlStateManager.scalef(1 + scaleFactor*0.07f, 1 - scaleFactor*0.05f, 1);
		
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		vertexbuffer.pos(-0.5D, -0.25D, 0.0D).tex(0.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.pos(0.5D, -0.25D, 0.0D).tex(1.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.pos(0.5D, 0.75D, 0.0D).tex(1.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.pos(-0.5D, 0.75D, 0.0D).tex(0.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}
	
	@Override
	protected ResourceLocation getEntityTexture(VitalityGelEntity entity)
	{
		return new ResourceLocation("minestuck", "textures/entity/vitality_gel.png");
	}

}
