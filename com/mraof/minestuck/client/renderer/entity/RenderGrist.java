package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.entity.item.EntityGrist;

public class RenderGrist extends Render {
	
	public RenderGrist()
	{
		this.shadowSize = 0.15F;
		this.shadowOpaque = .75F;
	}
	public void renderGrist(EntityGrist grist, double d0, double d1, double d2, float f, float f1)
	{
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d0, (float)d1, (float)d2);
//        this.loadTexture("Minestuck:/textures/grist/" + grist.getType() + ".png");
        this.func_110777_b(grist);
        Tessellator tessellator = Tessellator.instance;
        float f2 = 0.0F;
        float f3 = 1.0F;
        float f4 = 0.0F;
        float f5 = 1.0F;
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.25F;
        int j = grist.getBrightnessForRender(f1);
        int k = j % 65536;
        int l = j / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        float f11 = grist.getSizeByValue();
        GL11.glScalef(f11, f11, f11);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV((double)(0.0F - f7), (double)(0.0F - f8), 0.0D, (double)f2, (double)f5);
        tessellator.addVertexWithUV((double)(f6 - f7), (double)(0.0F - f8), 0.0D, (double)f3, (double)f5);
        tessellator.addVertexWithUV((double)(f6 - f7), (double)(1.0F - f8), 0.0D, (double)f3, (double)f4);
        tessellator.addVertexWithUV((double)(0.0F - f7), (double)(1.0F - f8), 0.0D, (double)f2, (double)f4);
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
		
	}
	@Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) 
	{
		renderGrist((EntityGrist) entity, d0, d1, d2, f, f1);
	}
	@Override
	protected ResourceLocation func_110775_a(Entity entity) 
	{
		return new ResourceLocation("Minestuck:/textures/grist/" + ((EntityGrist) entity).getType() + ".png");
	}

}
