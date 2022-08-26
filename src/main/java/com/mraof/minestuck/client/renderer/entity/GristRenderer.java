package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mraof.minestuck.entity.item.GristEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class GristRenderer extends EntityRenderer<GristEntity>
{
	static final double shakeScale = 1.0 / 256.0;
	public GristRenderer(EntityRendererProvider.Context context)
	{
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowStrength = .75F;
	}
	
	@Override
	public void render(GristEntity grist, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn)
	{
		poseStack.pushPose();
		poseStack.translate(0.0F, 0.0F + grist.getSizeByValue() / 2, 0.0F);
		poseStack.scale(grist.getSizeByValue(), grist.getSizeByValue(), grist.getSizeByValue());
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		
		RenderSystem.setShaderColor(255, 0, 0, 255);
		
		poseStack.translate(
				(Math.random() - 0.5) * Math.max(0, grist.shaderAlpha - 128) * shakeScale,
				(Math.random() - 0.5) * Math.max(0, grist.shaderAlpha - 128) * shakeScale,
				(Math.random() - 0.5) * Math.max(0, grist.shaderAlpha - 128) * shakeScale);
		
		PoseStack.Pose matrixstack = poseStack.last();
		Matrix4f matrix4f = matrixstack.pose();
		Matrix3f matrix3f = matrixstack.normal();
		VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(grist)));
		ivertexbuilder.vertex(matrix4f, 0.0F - 0.5F, 0 - 0.25F, 0.0F)
				.color(255, 255 - grist.shaderAlpha, 255 - grist.shaderAlpha, 255)
				.uv(0, 1)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		
		ivertexbuilder.vertex(matrix4f, 1.0F - 0.5F, 0 - 0.25F, 0.0F)
				.color(255, 255 - grist.shaderAlpha, 255 - grist.shaderAlpha, 255)
				.uv(1, 1)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		
		ivertexbuilder.vertex(matrix4f, 1.0F - 0.5F, 1 - 0.25F, 0.0F)
				.color(255, 255 - grist.shaderAlpha, 255 - grist.shaderAlpha, 255)
				.uv(1, 0)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		
		ivertexbuilder.vertex(matrix4f, 0.0F - 0.5F, 1 - 0.25F, 0.0F)
				.color(255, 255 - grist.shaderAlpha, 255 - grist.shaderAlpha, 255)
				.uv(0, 0)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		
		
		poseStack.popPose();
		super.render(grist, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}
	
	@Override
	public ResourceLocation getTextureLocation(GristEntity entity)
	{
		return entity.getGristType().getIcon();
	}
	
}
