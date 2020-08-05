package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.entity.item.HangingArtEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class RenderHangingArt<T extends HangingArtEntity<?>> extends EntityRenderer<T>
{
	private final ResourceLocation ART_TEXTURE;

	public RenderHangingArt(EntityRendererManager renderManagerIn, ResourceLocation artPath)
	{
		super(renderManagerIn);
		ART_TEXTURE = new ResourceLocation(artPath.getNamespace(), "textures/painting/" + artPath.getPath() + ".png");
	}

	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
		HangingArtEntity.IArt art = entityIn.art;
		matrixStackIn.scale(0.0625F, 0.0625F, 0.0625F);
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntitySolid(this.getEntityTexture(entityIn)));
		this.renderPainting(matrixStackIn, ivertexbuilder, entityIn, art.getSizeX(), art.getSizeY(), art.getOffsetX(), art.getOffsetY());
		matrixStackIn.pop();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(HangingArtEntity entity)
	{
		return ART_TEXTURE;
	}

	private void renderPainting(MatrixStack matrixStack, IVertexBuilder vertexBuilder, T painting, int width, int height, int textureU, int textureV) {
		MatrixStack.Entry matrixstackEntry = matrixStack.getLast();
		Matrix4f matrix4f = matrixstackEntry.getMatrix();
		Matrix3f matrix3f = matrixstackEntry.getNormal();
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
				int x = MathHelper.floor(painting.getPosX());
				int y = MathHelper.floor(painting.getPosY() + (double)((upperV + lowerV) / 2.0F / 16.0F));
				int z = MathHelper.floor(painting.getPosZ());
				int light = WorldRenderer.getCombinedLight(painting.world, new BlockPos(x, y, z));
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

	private void build(Matrix4f p_229121_1_, Matrix3f p_229121_2_, IVertexBuilder p_229121_3_, float p_229121_4_, float p_229121_5_, float p_229121_6_, float p_229121_7_, float p_229121_8_, int p_229121_9_, int p_229121_10_, int p_229121_11_, int p_229121_12_) {
		p_229121_3_.pos(p_229121_1_, p_229121_4_, p_229121_5_, p_229121_8_).color(255, 255, 255, 255).tex(p_229121_6_, p_229121_7_).overlay(OverlayTexture.NO_OVERLAY).lightmap(p_229121_12_).normal(p_229121_2_, (float)p_229121_9_, (float)p_229121_10_, (float)p_229121_11_).endVertex();
	}
}
