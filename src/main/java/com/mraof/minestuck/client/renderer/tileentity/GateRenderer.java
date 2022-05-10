package com.mraof.minestuck.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.tileentity.GateTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class GateRenderer extends TileEntityRenderer<GateTileEntity>
{

	private static final ResourceLocation INNER_NODE = new ResourceLocation("minestuck","textures/block/node_spiro_inner.png");
	private static final ResourceLocation OUTER_NODE = new ResourceLocation("minestuck","textures/block/node_spiro_outer.png");

	public GateRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(GateTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		int color = tileEntityIn.color;
		int r = ((color >> 16) & 255);
		int g = ((color >> 8) & 255);
		int b = (color & 255);

		float tick = tileEntityIn.getLevel().getGameTime() + partialTicks;

		if (tileEntityIn.isGate())
		{
			matrixStackIn.pushPose();
			matrixStackIn.translate(0.5F, 0.0F, 0.5F);
			matrixStackIn.mulPose(Vector3f.YP.rotation(tick / 75));
			MatrixStack.Entry matrixstack = matrixStackIn.last();
			Matrix4f matrix4f = matrixstack.pose();
			Matrix3f matrix3f = matrixstack.normal();
			float y = 0.5F;
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(INNER_NODE));
			ivertexbuilder.vertex(matrix4f, -1.5F, y, -1.5F).color(r, g, b, 255).uv(0, 0)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.vertex(matrix4f, -1.5F, y, 1.5F).color(r, g, b, 255).uv(0, 1)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.vertex(matrix4f, 1.5F, y, 1.5F).color(r, g, b, 255).uv(1, 1)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.vertex(matrix4f, 1.5F, y, -1.5F).color(r, g, b, 255).uv(1, 0)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			matrixStackIn.popPose();
		}
		else
		{
			matrixStackIn.pushPose();
			matrixStackIn.mulPose(Vector3f.YP.rotation(tick / 75));
			MatrixStack.Entry matrixstack = matrixStackIn.last();
			Matrix4f matrix4f = matrixstack.pose();
			Matrix3f matrix3f = matrixstack.normal();
			float y = 0.5F;
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(INNER_NODE));
			ivertexbuilder.vertex(matrix4f, -1F, y, -1F).color(r, g, b, 255).uv(0, 0)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.vertex(matrix4f, -1F, y, 1F).color(r, g, b, 255).uv(0, 1)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.vertex(matrix4f, 1F, y, 1F).color(r, g, b, 255).uv(1, 1)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.vertex(matrix4f, 1F, y, -1F).color(r, g, b, 255).uv(1, 0)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			matrixStackIn.popPose();

			matrixStackIn.pushPose();
			matrixStackIn.mulPose(Vector3f.YP.rotation(-(tick / 75) / 1.5F));
			MatrixStack.Entry matrixstack2 = matrixStackIn.last();
			Matrix4f matrix4f2 = matrixstack2.pose();
			Matrix3f matrix3f2 = matrixstack2.normal();
			y = (float) (0.5 + MathHelper.sin(tick/50)*0.1);
			IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(RenderType.entityCutoutNoCull(OUTER_NODE));
			ivertexbuilder2.vertex(matrix4f2, -1F, y, -1F).color(r, g, b, 255).uv(0, 0)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f2, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder2.vertex(matrix4f2, -1F, y, 1F).color(r, g, b, 255).uv(0, 1)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f2, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder2.vertex(matrix4f2, 1F, y, 1F).color(r, g, b, 255).uv(1, 1)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f2, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder2.vertex(matrix4f2, 1F, y, -1F).color(r, g, b, 255).uv(1, 0)
					.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(matrix3f2, 0.0F, 1.0F, 0.0F).endVertex();
			matrixStackIn.popPose();
		}
	}
}