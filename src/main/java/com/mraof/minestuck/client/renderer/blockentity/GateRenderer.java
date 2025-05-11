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
		float y = 0.5F;
		VertexConsumer consumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
		consumer.addVertex(matrix4f, -1.5F, y, -1.5F).setColor(r, g, b, 255).setUv(0, 0)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
		consumer.addVertex(matrix4f, -1.5F, y, 1.5F).setColor(r, g, b, 255).setUv(0, 1)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
		consumer.addVertex(matrix4f, 1.5F, y, 1.5F).setColor(r, g, b, 255).setUv(1, 1)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
		consumer.addVertex(matrix4f, 1.5F, y, -1.5F).setColor(r, g, b, 255).setUv(1, 0)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
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
