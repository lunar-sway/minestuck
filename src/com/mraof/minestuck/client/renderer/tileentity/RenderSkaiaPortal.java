package com.mraof.minestuck.client.renderer.tileentity;

import com.mraof.minestuck.tileentity.SkaiaPortalTileEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.Random;

public class RenderSkaiaPortal extends TileEntityRenderer<SkaiaPortalTileEntity>
{
	private static final ResourceLocation tunnel = new ResourceLocation("minestuck","textures/tunnel.png");
    private static final ResourceLocation particlefield = new ResourceLocation("minestuck","textures/particlefield.png");
    
    FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(16);
	
	@Override
	public void render(SkaiaPortalTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage)
	{
		Entity temp = new EntityTippedArrow(tileEntityIn.getWorld());
		Vec3d position = ActiveRenderInfo.projectViewFromEntity(temp, 0);	//TODO temp solution for removed getter
		
		float var9 = (float)this.rendererDispatcher.entityX;
		float var10 = (float)this.rendererDispatcher.entityY;
		float var11 = (float)this.rendererDispatcher.entityZ;
		GlStateManager.disableLighting();
        Random var12 = new Random(31100L);
        float var13 = 0.75F;

        for (int var14 = 0; var14 < 16; ++var14)
        {
			GlStateManager.pushMatrix();
            float var15 = 16 - var14;
            float var16 = 0.0625F;
            float var17 = 1.0F / (var15 + 1.0F);

            if (var14 == 0)
            {
                this.bindTexture(tunnel);
                var17 = 0.1F;
                var15 = 65.0F;
                var16 = 0.125F;
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }

            if (var14 == 1)
            {
                this.bindTexture(particlefield);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
                var16 = 0.5F;
            }

            float var18 = (float) (-(y + var13));
			float var19 = var18 + (float) position.y;
			float var20 = var18 + var15 + (float) position.y;
			float var21 = var19 / var20;
			var21 += (float) (y + var13);
			GlStateManager.translatef(var9, var21, var11);
			GlStateManager.texGenMode(GlStateManager.TexGen.S, GL11.GL_OBJECT_LINEAR);
			GlStateManager.texGenMode(GlStateManager.TexGen.T, GL11.GL_OBJECT_LINEAR);
			GlStateManager.texGenMode(GlStateManager.TexGen.R, GL11.GL_OBJECT_LINEAR);
			GlStateManager.texGenMode(GlStateManager.TexGen.Q, GL11.GL_EYE_LINEAR);
			GlStateManager.texGenParam(GlStateManager.TexGen.S, GL11.GL_OBJECT_PLANE, this.func_76907_a(1.0F, 0.0F, 0.0F, 0.0F));
			GlStateManager.texGenParam(GlStateManager.TexGen.T, GL11.GL_OBJECT_PLANE, this.func_76907_a(0.0F, 0.0F, 1.0F, 0.0F));
			GlStateManager.texGenParam(GlStateManager.TexGen.R, GL11.GL_OBJECT_PLANE, this.func_76907_a(0.0F, 0.0F, 0.0F, 1.0F));
			GlStateManager.texGenParam(GlStateManager.TexGen.Q, GL11.GL_EYE_PLANE, this.func_76907_a(0.0F, 1.0F, 0.0F, 0.0F));
			GlStateManager.enableTexGen(GlStateManager.TexGen.S);
			GlStateManager.enableTexGen(GlStateManager.TexGen.T);
			GlStateManager.enableTexGen(GlStateManager.TexGen.R);
			GlStateManager.enableTexGen(GlStateManager.TexGen.Q);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.loadIdentity();
			GlStateManager.translatef(0.0F, System.currentTimeMillis() % 700000L / 700000.0F, 0.0F);	//TODO Test if this is the right system time
			GlStateManager.scalef(var16, var16, var16);
			GlStateManager.translatef(0.5F, 0.5F, 0.0F);
			GlStateManager.rotatef((var14 * var14 * 4321 + var14 * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.translatef(-0.5F, -0.5F, 0.0F);
			GlStateManager.translatef(-var9, -var11, -var10);
			var19 = var18 + (float) position.x;
			GlStateManager.translatef(((float) position.x) * var15 / var19, ((float) position.z) * var15 / var19, -var10);
			BufferBuilder var24 = Tessellator.getInstance().getBuffer();
			var24.begin(7, DefaultVertexFormats.POSITION_TEX);
			var21 = var12.nextFloat() * 0.5F + 0.1F;
            float var22 = var12.nextFloat() * 0.5F + 0.4F;
            float var23 = var12.nextFloat() * 0.5F + 0.5F;

            if (var14 == 0)
            {
                var23 = 1.0F;
                var22 = 1.0F;
				var21 = 1.0F;
            }
			
			GlStateManager.color4f(var21 * var17, var22 * var17, var23 * var17, 1.0F);
			var24.pos(x, y + var13, z).tex(0, 0).endVertex();
			var24.pos(x, y + var13, z + 1.0D).tex(0, 1).endVertex();
			var24.pos(x + 1.0D, y + var13, z + 1.0D).tex(1, 1).endVertex();
			var24.pos(x + 1.0D, y + var13, z).tex(1, 0).endVertex();
			Tessellator.getInstance().draw();
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        }
		
		GlStateManager.disableBlend();
		GlStateManager.disableTexGen(GlStateManager.TexGen.S);
		GlStateManager.disableTexGen(GlStateManager.TexGen.T);
		GlStateManager.disableTexGen(GlStateManager.TexGen.R);
		GlStateManager.disableTexGen(GlStateManager.TexGen.Q);
		GlStateManager.enableLighting();
    }
    private FloatBuffer func_76907_a(float par1, float par2, float par3, float par4)
    {
        this.floatBuffer.clear();
        this.floatBuffer.put(par1).put(par2).put(par3).put(par4);
        this.floatBuffer.flip();
        return this.floatBuffer;
    }
}
