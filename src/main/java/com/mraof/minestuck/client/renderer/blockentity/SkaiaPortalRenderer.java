package com.mraof.minestuck.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.blockentity.SkaiaPortalBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SkaiaPortalRenderer implements BlockEntityRenderer<SkaiaPortalBlockEntity>
{
	private static final ResourceLocation tunnel = new ResourceLocation("minestuck","textures/tunnel.png");
    private static final ResourceLocation particlefield = new ResourceLocation("minestuck","textures/particlefield.png");
    
    //FloatBuffer floatBuffer = GLAllocation.createFloatBuffer(16);

	public SkaiaPortalRenderer(BlockEntityRendererProvider.Context context)
	{}

	@Override
	public void render(SkaiaPortalBlockEntity blockEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		/* TODO Restore this carefully. Make sure you understand the original renderer before attempting this
		Vec3 position = this.renderer.camera.getPosition();

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
                this.renderer.textureManager.bind(tunnel);
                var17 = 0.1F;
                var15 = 65.0F;
                var16 = 0.125F;
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }

            if (var14 == 1)
            {
				this.renderer.textureManager.bind(particlefield);
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
                var16 = 0.5F;
            }

            float var18 = (float) (-(blockEntityIn.getBlockPos().getY() + var13));
			float var19 = var18 + (float) position.y;
			float var20 = var18 + var15 + (float) position.y;
			float var21 = var19 / var20;
			var21 += (float) (blockEntityIn.getBlockPos().getY() + var13);
			RenderSystem.translatef(var9, var21, var11);
			GlStateManager._texGenMode(GlStateManager.TexGen.S, GL11.GL_OBJECT_LINEAR);
			GlStateManager._texGenMode(GlStateManager.TexGen.T, GL11.GL_OBJECT_LINEAR);
			GlStateManager._texGenMode(GlStateManager.TexGen.R, GL11.GL_OBJECT_LINEAR);
			GlStateManager._texGenMode(GlStateManager.TexGen.Q, GL11.GL_EYE_LINEAR);
			GlStateManager._texGenParam(GlStateManager.TexGen.S, GL11.GL_OBJECT_PLANE, this.func_76907_a(1.0F, 0.0F, 0.0F, 0.0F));
			GlStateManager._texGenParam(GlStateManager.TexGen.T, GL11.GL_OBJECT_PLANE, this.func_76907_a(0.0F, 0.0F, 1.0F, 0.0F));
			GlStateManager._texGenParam(GlStateManager.TexGen.R, GL11.GL_OBJECT_PLANE, this.func_76907_a(0.0F, 0.0F, 0.0F, 1.0F));
			GlStateManager._texGenParam(GlStateManager.TexGen.Q, GL11.GL_EYE_PLANE, this.func_76907_a(0.0F, 1.0F, 0.0F, 0.0F));
			GlStateManager._enableTexGen(GlStateManager.TexGen.S);
			GlStateManager._enableTexGen(GlStateManager.TexGen.T);
			GlStateManager._enableTexGen(GlStateManager.TexGen.R);
			GlStateManager._enableTexGen(GlStateManager.TexGen.Q);
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
			BufferBuilder var24 = Tessellator.getInstance().getBuilder();
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
			var24.vertex(blockEntityIn.getBlockPos().getX(), blockEntityIn.getBlockPos().getY() + var13, blockEntityIn.getBlockPos().getZ()).uv(0, 0).endVertex();
			var24.vertex(blockEntityIn.getBlockPos().getX(), blockEntityIn.getBlockPos().getY() + var13, blockEntityIn.getBlockPos().getZ() + 1.0D).uv(0, 1).endVertex();
			var24.vertex(blockEntityIn.getBlockPos().getX() + 1.0D, blockEntityIn.getBlockPos().getY() + var13, blockEntityIn.getBlockPos().getZ() + 1.0D).uv(1, 1).endVertex();
			var24.vertex(blockEntityIn.getBlockPos().getX() + 1.0D, blockEntityIn.getBlockPos().getY() + var13, blockEntityIn.getBlockPos().getZ()).uv(1, 0).endVertex();
			Tessellator.getInstance().end();
			RenderSystem.popMatrix();
			RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        }
		
		RenderSystem.disableBlend();
		GlStateManager._disableTexGen(GlStateManager.TexGen.S);
		GlStateManager._disableTexGen(GlStateManager.TexGen.T);
		GlStateManager._disableTexGen(GlStateManager.TexGen.R);
		GlStateManager._disableTexGen(GlStateManager.TexGen.Q);
		RenderSystem.enableLighting();
    }
    private FloatBuffer func_76907_a(float par1, float par2, float par3, float par4)
    {
        this.floatBuffer.clear();
        this.floatBuffer.put(par1).put(par2).put(par3).put(par4);
        this.floatBuffer.flip();
        return this.floatBuffer;*/
    }
}
