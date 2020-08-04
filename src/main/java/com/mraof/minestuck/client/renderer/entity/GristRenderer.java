package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.entity.item.GristEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL13;

public class GristRenderer extends EntityRenderer<GristEntity>
{
	
	public GristRenderer(EntityRendererManager manager)
	{
		super(manager);
		this.shadowSize = 0.15F;
		this.shadowOpaque = .75F;
	}

	@Override
	public void render(GristEntity grist, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		matrixStackIn.push();
		matrixStackIn.translate(0.0F, 0.0F + grist.getSizeByValue()/2, 0.0F);
		matrixStackIn.scale(grist.getSizeByValue(), grist.getSizeByValue(), grist.getSizeByValue());
		matrixStackIn.rotate(this.renderManager.getCameraOrientation());
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
		MatrixStack.Entry matrixstack = matrixStackIn.getLast();
		Matrix4f matrix4f = matrixstack.getMatrix();
		Matrix3f matrix3f = matrixstack.getNormal();
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(this.getEntityTexture(grist)));
		ivertexbuilder.pos(matrix4f, 0.0F - 0.5F, 0 - 0.25F, 0.0F).color(255, 255, 255, 255).tex(0, 1)
				.overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		ivertexbuilder.pos(matrix4f, 1.0F - 0.5F, 0 - 0.25F, 0.0F).color(255, 255, 255, 255).tex(1, 1)
				.overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		ivertexbuilder.pos(matrix4f, 1.0F - 0.5F, 1 - 0.25F, 0.0F).color(255, 255, 255, 255).tex(1, 0)
				.overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		ivertexbuilder.pos(matrix4f, 0.0F - 0.5F, 1 - 0.25F, 0.0F).color(255, 255, 255, 255).tex(0, 0)
				.overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		matrixStackIn.pop();
		super.render(grist, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(GristEntity entity) {
		return entity.getGristType().getIcon();
	}

}
