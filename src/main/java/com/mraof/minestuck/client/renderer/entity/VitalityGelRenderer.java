package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mraof.minestuck.entity.item.VitalityGelEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
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
		poseStack.translate(0.0F, 0.1F + gel.getSizeByValue() / 8, 0.0F);
		poseStack.scale(gel.getSizeByValue(), gel.getSizeByValue(), gel.getSizeByValue());
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		PoseStack.Pose matrixstack = poseStack.last();
		Matrix4f matrix4f = matrixstack.pose();
		VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(gel)));
		ivertexbuilder.addVertex(matrix4f, 0.0F - 0.5F, 0 - 0.25F, 0.0F).setColor(255, 255, 255, 255).setUv(0, 1)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLightIn).setNormal(matrixstack, 0.0F, 1.0F, 0.0F);
		ivertexbuilder.addVertex(matrix4f, 1.0F - 0.5F, 0 - 0.25F, 0.0F).setColor(255, 255, 255, 255).setUv(1, 1)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLightIn).setNormal(matrixstack, 0.0F, 1.0F, 0.0F);
		ivertexbuilder.addVertex(matrix4f, 1.0F - 0.5F, 1 - 0.25F, 0.0F).setColor(255, 255, 255, 255).setUv(1, 0)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLightIn).setNormal(matrixstack, 0.0F, 1.0F, 0.0F);
		ivertexbuilder.addVertex(matrix4f, 0.0F - 0.5F, 1 - 0.25F, 0.0F).setColor(255, 255, 255, 255).setUv(0, 0)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLightIn).setNormal(matrixstack, 0.0F, 1.0F, 0.0F);
		poseStack.popPose();
		super.render(gel, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}
	
	@Override
	public ResourceLocation getTextureLocation(VitalityGelEntity entity)
	{
		return ResourceLocation.fromNamespaceAndPath("minestuck", "textures/entity/vitality_gel.png");
	}
	
}
