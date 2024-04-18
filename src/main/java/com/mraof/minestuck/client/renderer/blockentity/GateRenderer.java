package com.mraof.minestuck.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mraof.minestuck.blockentity.GateBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@MethodsReturnNonnullByDefault
public class GateRenderer implements BlockEntityRenderer<GateBlockEntity>
{
	private static final ResourceLocation TEXTURE = ReturnNodeRenderer.INNER_NODE;
	
	public GateRenderer(BlockEntityRendererProvider.Context context)
	{}
	
	@Override
	public void render(GateBlockEntity blockEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		int color = blockEntityIn.color;
		int r = ((color >> 16) & 255);
		int g = ((color >> 8) & 255);
		int b = (color & 255);
		
		float tick = blockEntityIn.getLevel().getGameTime() + partialTicks;
		
		poseStack.pushPose();
		poseStack.translate(0.5F, 0.0F, 0.5F);
		poseStack.mulPose(Axis.YP.rotation(tick / 75));
		PoseStack.Pose pose = poseStack.last();
		Matrix4f matrix4f = pose.pose();
		Matrix3f matrix3f = pose.normal();
		float y = 0.5F;
		VertexConsumer consumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
		consumer.vertex(matrix4f, -1.5F, y, -1.5F).color(r, g, b, 255).uv(0, 0)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		consumer.vertex(matrix4f, -1.5F, y, 1.5F).color(r, g, b, 255).uv(0, 1)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		consumer.vertex(matrix4f, 1.5F, y, 1.5F).color(r, g, b, 255).uv(1, 1)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		consumer.vertex(matrix4f, 1.5F, y, -1.5F).color(r, g, b, 255).uv(1, 0)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		poseStack.popPose();
	}
	
	@Override
	public int getViewDistance()
	{
		return 256;
	}
	
	@Override
	public AABB getRenderBoundingBox(GateBlockEntity blockEntity)
	{
		Vec3 corner = Vec3.atLowerCornerOf(blockEntity.getBlockPos().offset(-1, 0, -1));
		return new AABB(corner, corner.add(3, 1, 3));
	}
}