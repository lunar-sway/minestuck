package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import com.mraof.minestuck.entity.item.EntityVitalityGel;

public class RenderVitalityGel extends Render<EntityVitalityGel>
{
	
	public RenderVitalityGel(RenderManager manager)
	{
		super(manager);
		this.shadowSize = 0.15F;
		this.shadowOpaque = .75F;
	}
	
	@Override
	public void doRender(EntityVitalityGel entity, double d0, double d1, double d2, float f, float f1)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)d0, (float)d1 + entity.getSizeByValue()/2, (float)d2);
		this.bindEntityTexture(entity);
		VertexBuffer vertexbuffer = Tessellator.getInstance().getBuffer();
		int j = entity.getBrightnessForRender(f1);
		int k = j % 65536;
		int l = j / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		float f11 = entity.getSizeByValue();
		GlStateManager.scale(f11, f11, f11);
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
	protected ResourceLocation getEntityTexture(EntityVitalityGel entity) 
	{
		return new ResourceLocation("minestuck", "textures/entity/vitality_gel.png");
	}

}
