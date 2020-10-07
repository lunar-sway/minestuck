package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.entity.item.VitalityGelEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class VitalityGelRenderer extends EntityRenderer<VitalityGelEntity>
{
	
	public VitalityGelRenderer(EntityRendererManager manager)
	{
		super(manager);
		this.shadowSize = 0.15F;
		this.shadowOpaque = .75F;
	}

	@Override
	public void render(VitalityGelEntity gel, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		matrixStackIn.push();
		matrixStackIn.translate(0.0F, 0.0F + gel.getSizeByValue()/2, 0.0F);
		matrixStackIn.scale(gel.getSizeByValue(), gel.getSizeByValue(), gel.getSizeByValue());
		matrixStackIn.rotate(this.renderManager.getCameraOrientation());
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
		MatrixStack.Entry matrixstack = matrixStackIn.getLast();
		Matrix4f matrix4f = matrixstack.getMatrix();
		Matrix3f matrix3f = matrixstack.getNormal();
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(this.getEntityTexture(gel)));
		ivertexbuilder.pos(matrix4f, 0.0F - 0.5F, 0 - 0.25F, 0.0F).color(255, 255, 255, 255).tex(0, 1)
				.overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		ivertexbuilder.pos(matrix4f, 1.0F - 0.5F, 0 - 0.25F, 0.0F).color(255, 255, 255, 255).tex(1, 1)
				.overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		ivertexbuilder.pos(matrix4f, 1.0F - 0.5F, 1 - 0.25F, 0.0F).color(255, 255, 255, 255).tex(1, 0)
				.overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		ivertexbuilder.pos(matrix4f, 0.0F - 0.5F, 1 - 0.25F, 0.0F).color(255, 255, 255, 255).tex(0, 0)
				.overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		matrixStackIn.pop();
		super.render(gel, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
	
//	@Override
//	public void doRender(VitalityGelEntity entity, double x, double y, double z, float entityYaw, float partialTicks)
//	{
//		GlStateManager.pushMatrix();
//		GlStateManager.translatef((float)x, (float)y + entity.getSizeByValue()/2, (float)z);
//		this.bindEntityTexture(entity);
//		BufferBuilder vertexbuffer = Tessellator.getInstance().getBuffer();
//		int j = entity.getBrightnessForRender();
//		int k = j % 65536;
//		int l = j / 65536;
//		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float)k, (float)l);
//		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//		GlStateManager.rotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
//		GlStateManager.rotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
//
//		float size = entity.getSizeByValue();
//		GlStateManager.scalef(size, size, size);
//		float scaleFactor = MathHelper.sin((entity.age + partialTicks)/ 10f + entity.animationOffset);
//		GlStateManager.scalef(1 + scaleFactor*0.07f, 1 - scaleFactor*0.05f, 1);
//
//		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
//		vertexbuffer.pos(-0.5D, -0.25D, 0.0D).tex(0.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
//		vertexbuffer.pos(0.5D, -0.25D, 0.0D).tex(1.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
//		vertexbuffer.pos(0.5D, 0.75D, 0.0D).tex(1.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
//		vertexbuffer.pos(-0.5D, 0.75D, 0.0D).tex(0.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
//		Tessellator.getInstance().draw();
//		GlStateManager.disableBlend();
//		GlStateManager.disableRescaleNormal();
//		GlStateManager.popMatrix();
//	}
//
	@Override
	public ResourceLocation getEntityTexture(VitalityGelEntity entity)
	{
		return new ResourceLocation("minestuck", "textures/entity/vitality_gel.png");
	}

}
