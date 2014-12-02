package com.mraof.minestuck.client.renderer.tileentity;

import java.nio.FloatBuffer;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.tileentity.TileEntityGatePortal;

public class RenderGatePortal extends TileEntitySpecialRenderer 
{
	private static final ResourceLocation tunnel = new ResourceLocation("minestuck","textures/tunnel.png");
    private static final ResourceLocation particlefield = new ResourceLocation("minestuck","textures/particlefield.png");
    
    FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(16);

	public void renderGatePortalTileEntity(TileEntityGatePortal par1TileEntityGatePortal, double par2, double par4, double par6, float par8)
    {
//        float var9 = (float)this.field_147501_a.field_147560_j;
//        float var10 = (float)this.field_147501_a.field_147561_k;
//        float var11 = (float)this.field_147501_a.field_147558_l;
        GL11.glDisable(GL11.GL_LIGHTING);
        Random var12 = new Random(31100L);
        float var13 = 0.75F;

        for (int var14 = 0; var14 < 16; ++var14)
        {
            GL11.glPushMatrix();
            float var15 = 16 - var14;
            float var16 = 0.0625F;
            float var17 = 1.0F / (var15 + 1.0F);

            if (var14 == 0)
            {
                this.bindTexture(tunnel);
                var17 = 0.1F;
                var15 = 65.0F;
                var16 = 0.125F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }

            if (var14 == 1)
            {
                this.bindTexture(particlefield);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                var16 = 0.5F;
            }

            float var18 = (float)(-(par4 + var13));
//            float var19 = var18 + ActiveRenderInfo.objectY;
//            float var20 = var18 + var15 + ActiveRenderInfo.objectY;
//            float var21 = var19 / var20;
//            var21 += (float)(par4 + var13);
//            GL11.glTranslatef(var9, var21, var11);
            GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
            GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, this.func_76907_a(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, this.func_76907_a(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(GL11.GL_R, GL11.GL_OBJECT_PLANE, this.func_76907_a(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, this.func_76907_a(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, Minecraft.getSystemTime() % 700000L / 700000.0F, 0.0F);
            GL11.glScalef(var16, var16, var16);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((var14 * var14 * 4321 + var14 * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
//            GL11.glTranslatef(-var9, -var11, -var10);
//            var19 = var18 + ActiveRenderInfo.objectY;
//            GL11.glTranslatef(ActiveRenderInfo.objectX * var15 / var19, ActiveRenderInfo.objectZ * var15 / var19, -var10);
			WorldRenderer var24 = Tessellator.getInstance().getWorldRenderer();
            var24.startDrawingQuads();
//            var21 = var12.nextFloat() * 0.5F + 0.1F;
            float var22 = var12.nextFloat() * 0.5F + 0.4F;
            float var23 = var12.nextFloat() * 0.5F + 0.5F;

            if (var14 == 0)
            {
                var23 = 1.0F;
                var22 = 1.0F;
//                var21 = 1.0F;
            }

//            var24.setColorRGBA_F(var21 * var17, var22 * var17, var23 * var17, 1.0F);
            var24.addVertex(par2, par4 + var13, par6);
            var24.addVertex(par2, par4 + var13, par6 + 1.0D);
            var24.addVertex(par2 + 1.0D, par4 + var13, par6 + 1.0D);
            var24.addVertex(par2 + 1.0D, par4 + var13, par6);
            var24.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
    private FloatBuffer func_76907_a(float par1, float par2, float par3, float par4)
    {
        this.floatBuffer.clear();
        this.floatBuffer.put(par1).put(par2).put(par3).put(par4);
        this.floatBuffer.flip();
        return this.floatBuffer;
    }
	
	@Override
	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8, int par9)
	{
		this.renderGatePortalTileEntity((TileEntityGatePortal)par1TileEntity, par2, par4, par6, par8);
	}
	
}
