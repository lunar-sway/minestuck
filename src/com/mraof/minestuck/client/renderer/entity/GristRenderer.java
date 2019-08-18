package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.item.GristEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GristRenderer extends EntityRenderer<GristEntity>
{
	
	public GristRenderer(EntityRendererManager manager)
	{
		super(manager);
		this.shadowSize = 0.15F;
		this.shadowOpaque = .75F;
	}

	@Override
	public void doRender(GristEntity grist, double d0, double d1, double d2, float f, float f1)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translatef((float)d0, (float)d1 + grist.getSizeByValue()/2, (float)d2);
		this.bindEntityTexture(grist);
		BufferBuilder vertexbuffer = Tessellator.getInstance().getBuffer();
		int j = grist.getBrightnessForRender();
		int k = j % 65536;
		int l = j / 65536;
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float)k, (float)l);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		float f11 = grist.getSizeByValue();
		GlStateManager.scalef(f11, f11, f11);
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
	protected ResourceLocation getEntityTexture(GristEntity entity)
	{
		return entity.getGristType().getIcon();
	}

}
