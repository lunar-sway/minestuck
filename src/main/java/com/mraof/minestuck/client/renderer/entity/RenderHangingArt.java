package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mraof.minestuck.entity.item.HangingArtEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderHangingArt<T extends HangingArtEntity<?>> extends EntityRenderer<T>
{
	private final ResourceLocation ART_TEXTURE;

	public RenderHangingArt(EntityRendererProvider.Context context, ResourceLocation artPath)
	{
		super(context);
		ART_TEXTURE = new ResourceLocation(artPath.getNamespace(), "textures/painting/" + artPath.getPath() + ".png");
	}

	public void render(T entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
		HangingArtEntity.IArt art = entityIn.getArt();
		poseStack.scale(0.0625F, 0.0625F, 0.0625F);
		VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entitySolid(this.getTextureLocation(entityIn)));
		this.renderPainting(poseStack, ivertexbuilder, entityIn, art.getSizeX(), art.getSizeY(), art.getOffsetX(), art.getOffsetY());
		poseStack.popPose();
		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(HangingArtEntity entity)
	{
		return ART_TEXTURE;
	}

	private void renderPainting(PoseStack poseStack, VertexConsumer vertexBuilder, T painting, int width, int height, int textureU, int textureV) {
		PoseStack.Pose matrixstackEntry = poseStack.last();
		Matrix4f matrix4f = matrixstackEntry.pose();
		Matrix3f matrix3f = matrixstackEntry.normal();
		float renderOffsetU = (float) (-width) / 2.0F;
		float renderOffsetV = (float) (-height) / 2.0F;

		for (int blockU = 0; blockU < width / 16; blockU++)
		{
			for (int blockV = 0; blockV < height / 16; blockV++)
			{
				float upperU = renderOffsetU + (float) ((blockU + 1) * 16);
				float lowerU = renderOffsetU + (float) (blockU * 16);
				float upperV = renderOffsetV + (float) ((blockV + 1) * 16);
				float lowerV = renderOffsetV + (float) (blockV * 16);
				int x = Mth.floor(painting.getX());
				int y = Mth.floor(painting.getY() + (double)((upperV + lowerV) / 2.0F / 16.0F));
				int z = Mth.floor(painting.getZ());
				int light = LevelRenderer.getLightColor(painting.level(), new BlockPos(x, y, z));
				float f19 = (float) (textureU + width - blockU * 16) / 256.0F;
				float f20 = (float) (textureU + width - (blockU + 1) * 16) / 256.0F;
				float f21 = (float) (textureV + height - blockV * 16) / 256.0F;
				float f22 = (float) (textureV + height - (blockV + 1) * 16) / 256.0F;
				this.build(matrix4f, matrix3f, vertexBuilder, upperU, lowerV, f20, f21, -0.5F, 0, 0, -1, light);
				this.build(matrix4f, matrix3f, vertexBuilder, lowerU, lowerV, f19, f21, -0.5F, 0, 0, -1, light);
				this.build(matrix4f, matrix3f, vertexBuilder, lowerU, upperV, f19, f22, -0.5F, 0, 0, -1, light);
				this.build(matrix4f, matrix3f, vertexBuilder, upperU, upperV, f20, f22, -0.5F, 0, 0, -1, light);
				this.build(matrix4f, matrix3f, vertexBuilder, upperU, upperV, 0.75F, 0.0F, 0.5F, 0, 0, 1, light);
				this.build(matrix4f, matrix3f, vertexBuilder, lowerU, upperV, 0.8125F, 0.0F, 0.5F, 0, 0, 1, light);
				this.build(matrix4f, matrix3f, vertexBuilder, lowerU, lowerV, 0.8125F, 0.0625F, 0.5F, 0, 0, 1, light);
				this.build(matrix4f, matrix3f, vertexBuilder, upperU, lowerV, 0.75F, 0.0625F, 0.5F, 0, 0, 1, light);
				this.build(matrix4f, matrix3f, vertexBuilder, upperU, upperV, 0.75F, 0.001953125F, -0.5F, 0, 1, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, lowerU, upperV, 0.8125F, 0.001953125F, -0.5F, 0, 1, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, lowerU, upperV, 0.8125F, 0.001953125F, 0.5F, 0, 1, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, upperU, upperV, 0.75F, 0.001953125F, 0.5F, 0, 1, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, upperU, lowerV, 0.75F, 0.001953125F, 0.5F, 0, -1, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, lowerU, lowerV, 0.8125F, 0.001953125F, 0.5F, 0, -1, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, lowerU, lowerV, 0.8125F, 0.001953125F, -0.5F, 0, -1, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, upperU, lowerV, 0.75F, 0.001953125F, -0.5F, 0, -1, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, upperU, upperV, 0.751953125F, 0.0F, 0.5F, -1, 0, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, upperU, lowerV, 0.751953125F, 0.0625F, 0.5F, -1, 0, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, upperU, lowerV, 0.751953125F, 0.0625F, -0.5F, -1, 0, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, upperU, upperV, 0.751953125F, 0.0F, -0.5F, -1, 0, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, lowerU, upperV, 0.751953125F, 0.0F, -0.5F, 1, 0, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, lowerU, lowerV, 0.751953125F, 0.0625F, -0.5F, 1, 0, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, lowerU, lowerV, 0.751953125F, 0.0625F, 0.5F, 1, 0, 0, light);
				this.build(matrix4f, matrix3f, vertexBuilder, lowerU, upperV, 0.751953125F, 0.0F, 0.5F, 1, 0, 0, light);
			}
		}
	}

	private void build(Matrix4f p_229121_1_, Matrix3f p_229121_2_, VertexConsumer p_229121_3_, float p_229121_4_, float p_229121_5_, float p_229121_6_, float p_229121_7_, float p_229121_8_, int p_229121_9_, int p_229121_10_, int p_229121_11_, int p_229121_12_) {
		p_229121_3_.vertex(p_229121_1_, p_229121_4_, p_229121_5_, p_229121_8_).color(255, 255, 255, 255).uv(p_229121_6_, p_229121_7_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229121_12_).normal(p_229121_2_, (float)p_229121_9_, (float)p_229121_10_, (float)p_229121_11_).endVertex();
	}
}
