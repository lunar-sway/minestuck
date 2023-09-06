package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mraof.minestuck.entity.item.VitalityGelEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class VitalityGelRenderer extends EntityRenderer<VitalityGelEntity>
{
	
	public VitalityGelRenderer(EntityRendererProvider.Context context)
	{
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowStrength = .75F;
	}

	@Override
	public void render(VitalityGelEntity gel, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn)
	{
		poseStack.pushPose();
		poseStack.translate(0.0F, 0.0F + gel.getSizeByValue()/2, 0.0F);
		poseStack.scale(gel.getSizeByValue(), gel.getSizeByValue(), gel.getSizeByValue());
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		PoseStack.Pose matrixstack = poseStack.last();
		Matrix4f matrix4f = matrixstack.pose();
		Matrix3f matrix3f = matrixstack.normal();
		VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(gel)));
		ivertexbuilder.vertex(matrix4f, 0.0F - 0.5F, 0 - 0.25F, 0.0F).color(255, 255, 255, 255).uv(0, 1)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		ivertexbuilder.vertex(matrix4f, 1.0F - 0.5F, 0 - 0.25F, 0.0F).color(255, 255, 255, 255).uv(1, 1)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		ivertexbuilder.vertex(matrix4f, 1.0F - 0.5F, 1 - 0.25F, 0.0F).color(255, 255, 255, 255).uv(1, 0)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		ivertexbuilder.vertex(matrix4f, 0.0F - 0.5F, 1 - 0.25F, 0.0F).color(255, 255, 255, 255).uv(0, 0)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		poseStack.popPose();
		super.render(gel, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
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
	public ResourceLocation getTextureLocation(VitalityGelEntity entity)
	{
		return new ResourceLocation("minestuck", "textures/entity/vitality_gel.png");
	}

}
