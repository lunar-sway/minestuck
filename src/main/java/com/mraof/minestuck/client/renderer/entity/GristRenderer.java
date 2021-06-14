package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.entity.item.GristEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class GristRenderer extends EntityRenderer<GristEntity>
{
	
	public GristRenderer(EntityRendererManager manager)
	{
		super(manager);
		this.shadowRadius = 0.15F;
		this.shadowStrength = .75F;
	}

	@Override
	public void render(GristEntity grist, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		matrixStackIn.pushPose();
		matrixStackIn.translate(0.0F, 0.0F + grist.getSizeByValue()/2, 0.0F);
		matrixStackIn.scale(grist.getSizeByValue(), grist.getSizeByValue(), grist.getSizeByValue());
		matrixStackIn.mulPose(this.entityRenderDispatcher.cameraOrientation());
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		MatrixStack.Entry matrixstack = matrixStackIn.last();
		Matrix4f matrix4f = matrixstack.pose();
		Matrix3f matrix3f = matrixstack.normal();
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(grist)));
		ivertexbuilder.vertex(matrix4f, 0.0F - 0.5F, 0 - 0.25F, 0.0F).color(255, 255, 255, 255).uv(0, 1)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		ivertexbuilder.vertex(matrix4f, 1.0F - 0.5F, 0 - 0.25F, 0.0F).color(255, 255, 255, 255).uv(1, 1)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		ivertexbuilder.vertex(matrix4f, 1.0F - 0.5F, 1 - 0.25F, 0.0F).color(255, 255, 255, 255).uv(1, 0)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		ivertexbuilder.vertex(matrix4f, 0.0F - 0.5F, 1 - 0.25F, 0.0F).color(255, 255, 255, 255).uv(0, 0)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		matrixStackIn.popPose();
		super.render(grist, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(GristEntity entity) {
		return entity.getGristType().getIcon();
	}

}
