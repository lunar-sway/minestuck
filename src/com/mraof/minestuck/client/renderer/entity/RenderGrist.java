package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.mraof.minestuck.entity.item.EntityGrist;

public class RenderGrist extends Render {
	
	public RenderGrist(RenderManager manager)
	{
		super(manager);
		this.shadowSize = 0.15F;
		this.shadowOpaque = .75F;
	}
	
	public void renderGrist(EntityGrist grist, double d0, double d1, double d2, float f, float f1)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)d0, (float)d1 + grist.getSizeByValue()/2, (float)d2);
		this.bindEntityTexture(grist);
		VertexBuffer vertexbuffer = Tessellator.getInstance().getBuffer();
		int j = grist.getBrightnessForRender(f1);
		int k = j % 65536;
		int l = j / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		float f11 = grist.getSizeByValue();
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
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) 
	{
		renderGrist((EntityGrist) entity, d0, d1, d2, f, f1);
	}
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) 
	{
		return new ResourceLocation("minestuck", "textures/grist/" + ((EntityGrist) entity).getType() + ".png");
	}

}
