package com.mraof.minestuck.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.tileentity.SkaiaPortalTileEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.Random;

public class SkaiaPortalRenderer extends TileEntityRenderer<SkaiaPortalTileEntity>
{
	private static final ResourceLocation tunnel = new ResourceLocation("minestuck","textures/tunnel.png");
    private static final ResourceLocation particlefield = new ResourceLocation("minestuck","textures/particlefield.png");
    
    FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(16);

	public SkaiaPortalRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(SkaiaPortalTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		Vec3d position = this.renderDispatcher.renderInfo.getProjectedView();

		float var9 = (float) position.x;
		float var10 = (float) position.y;
		float var11 = (float) position.z;
		RenderSystem.disableLighting();
        Random var12 = new Random(31100L);
        float var13 = 0.75F;

        for (int var14 = 0; var14 < 16; ++var14)
        {
			RenderSystem.pushMatrix();
            float var15 = 16 - var14;
            float var16 = 0.0625F;
            float var17 = 1.0F / (var15 + 1.0F);

            if (var14 == 0)
            {
                this.renderDispatcher.textureManager.bindTexture(tunnel);
                var17 = 0.1F;
                var15 = 65.0F;
                var16 = 0.125F;
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }

            if (var14 == 1)
            {
				this.renderDispatcher.textureManager.bindTexture(particlefield);
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
                var16 = 0.5F;
            }

            float var18 = (float) (-(tileEntityIn.getPos().getY() + var13));
			float var19 = var18 + (float) position.y;
			float var20 = var18 + var15 + (float) position.y;
			float var21 = var19 / var20;
			var21 += (float) (tileEntityIn.getPos().getY() + var13);
			RenderSystem.translatef(var9, var21, var11);
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
			RenderSystem.popMatrix();
			RenderSystem.matrixMode(GL11.GL_TEXTURE);
			RenderSystem.pushMatrix();
			RenderSystem.loadIdentity();
			RenderSystem.translatef(0.0F, System.currentTimeMillis() % 700000L / 700000.0F, 0.0F);	//TODO Test if this is the right system time
			RenderSystem.scalef(var16, var16, var16);
			RenderSystem.translatef(0.5F, 0.5F, 0.0F);
			RenderSystem.rotatef((var14 * var14 * 4321 + var14 * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			RenderSystem.translatef(-0.5F, -0.5F, 0.0F);
			RenderSystem.translatef(-var9, -var11, -var10);
			var19 = var18 + (float) position.x;
			RenderSystem.translatef(((float) position.x) * var15 / var19, ((float) position.z) * var15 / var19, -var10);
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
			
			RenderSystem.color4f(var21 * var17, var22 * var17, var23 * var17, 1.0F);
			var24.pos(tileEntityIn.getPos().getX(), tileEntityIn.getPos().getY() + var13, tileEntityIn.getPos().getZ()).tex(0, 0).endVertex();
			var24.pos(tileEntityIn.getPos().getX(), tileEntityIn.getPos().getY() + var13, tileEntityIn.getPos().getZ() + 1.0D).tex(0, 1).endVertex();
			var24.pos(tileEntityIn.getPos().getX() + 1.0D, tileEntityIn.getPos().getY() + var13, tileEntityIn.getPos().getZ() + 1.0D).tex(1, 1).endVertex();
			var24.pos(tileEntityIn.getPos().getX() + 1.0D, tileEntityIn.getPos().getY() + var13, tileEntityIn.getPos().getZ()).tex(1, 0).endVertex();
			Tessellator.getInstance().draw();
			RenderSystem.popMatrix();
			RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        }
		
		RenderSystem.disableBlend();
		GlStateManager.disableTexGen(GlStateManager.TexGen.S);
		GlStateManager.disableTexGen(GlStateManager.TexGen.T);
		GlStateManager.disableTexGen(GlStateManager.TexGen.R);
		GlStateManager.disableTexGen(GlStateManager.TexGen.Q);
		RenderSystem.enableLighting();
    }
    private FloatBuffer func_76907_a(float par1, float par2, float par3, float par4)
    {
        this.floatBuffer.clear();
        this.floatBuffer.put(par1).put(par2).put(par3).put(par4);
        this.floatBuffer.flip();
        return this.floatBuffer;
    }
}
