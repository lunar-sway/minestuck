package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mraof.minestuck.entity.item.GristEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class GristRenderer extends EntityRenderer<GristEntity>
{
	static final double SHAKE_INTENSITY = 0.7;
	
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
		poseStack.translate(0.0F, 0.1F + grist.getSizeByValue() / 8, 0.0F);
		poseStack.scale(grist.getSizeByValue(), grist.getSizeByValue(), grist.getSizeByValue());
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		
		poseStack.translate(
				(Math.random() - 0.5) * Math.max(0, (grist.getShakeFactor() - 0.5) * SHAKE_INTENSITY),
				(Math.random() - 0.5) * Math.max(0, (grist.getShakeFactor() - 0.5) * SHAKE_INTENSITY),
				(Math.random() - 0.5) * Math.max(0, (grist.getShakeFactor() - 0.5) * SHAKE_INTENSITY));
		
		PoseStack.Pose matrixstack = poseStack.last();
		Matrix4f matrix4f = matrixstack.pose();
		VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(grist)));
		
		float gbColor = 1 - grist.getShakeFactor();
		ivertexbuilder.addVertex(matrix4f, 0.0F - 0.5F, 0 - 0.25F, 0.0F)
				.setColor(1, gbColor, gbColor, 1)
				.setUv(0, 1)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLightIn).setNormal(matrixstack, 0.0F, 1.0F, 0.0F);
		
		ivertexbuilder.addVertex(matrix4f, 1.0F - 0.5F, 0 - 0.25F, 0.0F)
				.setColor(1, gbColor, gbColor, 1)
				.setUv(1, 1)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLightIn).setNormal(matrixstack, 0.0F, 1.0F, 0.0F);
		
		ivertexbuilder.addVertex(matrix4f, 1.0F - 0.5F, 1 - 0.25F, 0.0F)
				.setColor(1, gbColor, gbColor, 1)
				.setUv(1, 0)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLightIn).setNormal(matrixstack, 0.0F, 1.0F, 0.0F);
		
		ivertexbuilder.addVertex(matrix4f, 0.0F - 0.5F, 1 - 0.25F, 0.0F)
				.setColor(1, gbColor, gbColor, 1)
				.setUv(0, 0)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLightIn).setNormal(matrixstack, 0.0F, 1.0F, 0.0F);
		
		
		poseStack.popPose();
		super.render(grist, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}
	
	@Override
	public ResourceLocation getTextureLocation(GristEntity entity)
	{
		return entity.getGristType().getIcon();
	}
	
}
