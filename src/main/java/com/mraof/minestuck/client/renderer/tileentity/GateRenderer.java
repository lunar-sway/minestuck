package com.mraof.minestuck.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.tileentity.GateTileEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

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

		float tick = tileEntityIn.getWorld().getGameTime() + partialTicks;

		if (tileEntityIn.isGate())
		{
			matrixStackIn.push();
			matrixStackIn.translate(0.5F, 0.0F, 0.5F);
			matrixStackIn.rotate(Vector3f.YP.rotation(tick / 75));
			MatrixStack.Entry matrixstack = matrixStackIn.getLast();
			Matrix4f matrix4f = matrixstack.getMatrix();
			Matrix3f matrix3f = matrixstack.getNormal();
			float y = 0.5F;
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(INNER_NODE));
			ivertexbuilder.pos(matrix4f, -1.5F, y, -1.5F).color(r, g, b, 255).tex(0, 0)
					.overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.pos(matrix4f, -1.5F, y, 1.5F).color(r, g, b, 255).tex(0, 1)
					.overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.pos(matrix4f, 1.5F, y, 1.5F).color(r, g, b, 255).tex(1, 1)
					.overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.pos(matrix4f, 1.5F, y, -1.5F).color(r, g, b, 255).tex(1, 0)
					.overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			matrixStackIn.pop();
		}
		else
		{
			matrixStackIn.push();
			matrixStackIn.rotate(Vector3f.YP.rotation(tick / 75));
			MatrixStack.Entry matrixstack = matrixStackIn.getLast();
			Matrix4f matrix4f = matrixstack.getMatrix();
			Matrix3f matrix3f = matrixstack.getNormal();
			float y = 0.5F;
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(INNER_NODE));
			ivertexbuilder.pos(matrix4f, -1F, y, -1F).color(r, g, b, 255).tex(0, 0)
					.overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.pos(matrix4f, -1F, y, 1F).color(r, g, b, 255).tex(0, 1)
					.overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.pos(matrix4f, 1F, y, 1F).color(r, g, b, 255).tex(1, 1)
					.overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder.pos(matrix4f, 1F, y, -1F).color(r, g, b, 255).tex(1, 0)
					.overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
			matrixStackIn.pop();

			matrixStackIn.push();
			matrixStackIn.rotate(Vector3f.YP.rotation(-(tick / 75) / 1.5F));
			MatrixStack.Entry matrixstack2 = matrixStackIn.getLast();
			Matrix4f matrix4f2 = matrixstack2.getMatrix();
			Matrix3f matrix3f2 = matrixstack2.getNormal();
			y = (float) (0.5 + MathHelper.sin(tick/50)*0.1);
			IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(OUTER_NODE));
			ivertexbuilder2.pos(matrix4f2, -1F, y, -1F).color(r, g, b, 255).tex(0, 0)
					.overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(matrix3f2, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder2.pos(matrix4f2, -1F, y, 1F).color(r, g, b, 255).tex(0, 1)
					.overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(matrix3f2, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder2.pos(matrix4f2, 1F, y, 1F).color(r, g, b, 255).tex(1, 1)
					.overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(matrix3f2, 0.0F, 1.0F, 0.0F).endVertex();
			ivertexbuilder2.pos(matrix4f2, 1F, y, -1F).color(r, g, b, 255).tex(1, 0)
					.overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(matrix3f2, 0.0F, 1.0F, 0.0F).endVertex();
			matrixStackIn.pop();
		}
	}
}