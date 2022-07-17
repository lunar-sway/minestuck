package com.mraof.minestuck.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mraof.minestuck.tileentity.GateTileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GateRenderer implements BlockEntityRenderer<GateTileEntity>
{

	private static final ResourceLocation INNER_NODE = new ResourceLocation("minestuck","textures/block/node_spiro_inner.png");
	private static final ResourceLocation OUTER_NODE = new ResourceLocation("minestuck","textures/block/node_spiro_outer.png");
	
	public GateRenderer(BlockEntityRendererProvider.Context context)
	{}
	
	@Override
	public void render(GateTileEntity tileEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		int color = tileEntityIn.color;
		int r = ((color >> 16) & 255);
		int g = ((color >> 8) & 255);
		int b = (color & 255);

		float tick = tileEntityIn.getLevel().getGameTime() + partialTicks;

		if (tileEntityIn.isGate())
		{
			poseStack.pushPose();
			poseStack.translate(0.5F, 0.0F, 0.5F);
			poseStack.mulPose(Vector3f.YP.rotation(tick / 75));
			PoseStack.Pose pose = poseStack.last();
			Matrix4f matrix4f = pose.pose();
			Matrix3f matrix3f = pose.normal();
			float y = 0.5F;
			VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(INNER_NODE));
			ivertexbuilder.vertex(matrix4f, -1.5F, y, -1.5F).color(r, g, b, 255).uv(0, 0)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.vertex(matrix4f, -1.5F, y, 1.5F).color(r, g, b, 255).uv(0, 1)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.vertex(matrix4f, 1.5F, y, 1.5F).color(r, g, b, 255).uv(1, 1)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.vertex(matrix4f, 1.5F, y, -1.5F).color(r, g, b, 255).uv(1, 0)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			poseStack.popPose();
		}
		else
		{
			poseStack.pushPose();
			poseStack.mulPose(Vector3f.YP.rotation(tick / 75));
			PoseStack.Pose pose = poseStack.last();
			Matrix4f matrix4f = pose.pose();
			Matrix3f matrix3f = pose.normal();
			float y = 0.5F;
			VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(INNER_NODE));
			ivertexbuilder.vertex(matrix4f, -1F, y, -1F).color(r, g, b, 255).uv(0, 0)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.vertex(matrix4f, -1F, y, 1F).color(r, g, b, 255).uv(0, 1)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.vertex(matrix4f, 1F, y, 1F).color(r, g, b, 255).uv(1, 1)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.vertex(matrix4f, 1F, y, -1F).color(r, g, b, 255).uv(1, 0)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			poseStack.popPose();

			poseStack.pushPose();
			poseStack.mulPose(Vector3f.YP.rotation(-(tick / 75) / 1.5F));
			PoseStack.Pose pose2 = poseStack.last();
			Matrix4f matrix4f2 = pose2.pose();
			Matrix3f matrix3f2 = pose2.normal();
			y = (float) (0.5 + Mth.sin(tick/50)*0.1);
			VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(RenderType.entityCutoutNoCull(OUTER_NODE));
			ivertexbuilder2.vertex(matrix4f2, -1F, y, -1F).color(r, g, b, 255).uv(0, 0)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f2, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder2.vertex(matrix4f2, -1F, y, 1F).color(r, g, b, 255).uv(0, 1)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f2, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder2.vertex(matrix4f2, 1F, y, 1F).color(r, g, b, 255).uv(1, 1)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f2, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder2.vertex(matrix4f2, 1F, y, -1F).color(r, g, b, 255).uv(1, 0)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f2, 0.0F, 1.0F, 0.0F).endVertex();
			poseStack.popPose();
		}
	}
	
	@Override
	public int getViewDistance()
	{
		return 256;
	}
}