package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.entity.item.HangingArtEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
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
	
	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translated(x, y, z);
		GlStateManager.rotatef(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.enableRescaleNormal();
		this.bindEntityTexture(entity);
		HangingArtEntity.IArt art = entity.art;
		GlStateManager.scalef(0.0625F, 0.0625F, 0.0625F);
		
		if (this.renderOutlines)
		{
			GlStateManager.enableColorMaterial();
			GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
		}
		
		this.renderPainting(entity, art.getSizeX(), art.getSizeY(), art.getOffsetX(), art.getOffsetY());
		
		if (this.renderOutlines)
		{
			GlStateManager.tearDownSolidRenderingTextureCombine();
			GlStateManager.disableColorMaterial();
		}
		
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(HangingArtEntity entity)
	{
		return ART_TEXTURE;
	}
	
	private void renderPainting(T painting, int width, int height, int textureU, int textureV)
	{
		float renderOffsetU = (float)(-width) / 2.0F;
		float renderOffsetV = (float)(-height) / 2.0F;
		
		for (int blockU = 0; blockU < width / 16; blockU++)
		{
			for (int blockV = 0; blockV < height / 16; blockV++)
			{
				float lowerU = renderOffsetU + (float)(blockU * 16);
				float upperU = renderOffsetU + (float)((blockU + 1) * 16);
				float lowerV = renderOffsetV + (float)(blockV * 16);
				float upperV = renderOffsetV + (float)((blockV + 1) * 16);
				this.setLightmap(painting, (upperU + lowerU) / 2.0F, (upperV + lowerV) / 2.0F);
				float f19 = (float)(textureU + width - blockU * 16) / 256.0F;
				float f20 = (float)(textureU + width - (blockU + 1) * 16) / 256.0F;
				float f21 = (float)(textureV + height - blockV * 16) / 256.0F;
				float f22 = (float)(textureV + height - (blockV + 1) * 16) / 256.0F;
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				buffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
				buffer.pos(upperU, lowerV, -0.5D).tex(f20, f21).normal(0.0F, 0.0F, -1.0F).endVertex();
				buffer.pos(lowerU, lowerV, -0.5D).tex(f19, f21).normal(0.0F, 0.0F, -1.0F).endVertex();
				buffer.pos(lowerU, upperV, -0.5D).tex(f19, f22).normal(0.0F, 0.0F, -1.0F).endVertex();
				buffer.pos(upperU, upperV, -0.5D).tex(f20, f22).normal(0.0F, 0.0F, -1.0F).endVertex();
				buffer.pos(upperU, upperV, 0.5D).tex(0.75D, 0.0D).normal(0.0F, 0.0F, 1.0F).endVertex();
				buffer.pos(lowerU, upperV, 0.5D).tex(0.8125D, 0.0D).normal(0.0F, 0.0F, 1.0F).endVertex();
				buffer.pos(lowerU, lowerV, 0.5D).tex(0.8125D, 0.0625D).normal(0.0F, 0.0F, 1.0F).endVertex();
				buffer.pos(upperU, lowerV, 0.5D).tex(0.75D, 0.0625D).normal(0.0F, 0.0F, 1.0F).endVertex();
				buffer.pos(upperU, upperV, -0.5D).tex(0.75D, 0.001953125D).normal(0.0F, 1.0F, 0.0F).endVertex();
				buffer.pos(lowerU, upperV, -0.5D).tex(0.8125D, 0.001953125D).normal(0.0F, 1.0F, 0.0F).endVertex();
				buffer.pos(lowerU, upperV, 0.5D).tex(0.8125D, 0.001953125D).normal(0.0F, 1.0F, 0.0F).endVertex();
				buffer.pos(upperU, upperV, 0.5D).tex(0.75D, 0.001953125D).normal(0.0F, 1.0F, 0.0F).endVertex();
				buffer.pos(upperU, lowerV, 0.5D).tex(0.75D, 0.001953125D).normal(0.0F, -1.0F, 0.0F).endVertex();
				buffer.pos(lowerU, lowerV, 0.5D).tex(0.8125D, 0.001953125D).normal(0.0F, -1.0F, 0.0F).endVertex();
				buffer.pos(lowerU, lowerV, -0.5D).tex(0.8125D, 0.001953125D).normal(0.0F, -1.0F, 0.0F).endVertex();
				buffer.pos(upperU, lowerV, -0.5D).tex(0.75D, 0.001953125D).normal(0.0F, -1.0F, 0.0F).endVertex();
				buffer.pos(upperU, upperV, 0.5D).tex(0.751953125D, 0.0D).normal(-1.0F, 0.0F, 0.0F).endVertex();
				buffer.pos(upperU, lowerV, 0.5D).tex(0.751953125D, 0.0625D).normal(-1.0F, 0.0F, 0.0F).endVertex();
				buffer.pos(upperU, lowerV, -0.5D).tex(0.751953125D, 0.0625D).normal(-1.0F, 0.0F, 0.0F).endVertex();
				buffer.pos(upperU, upperV, -0.5D).tex(0.751953125D, 0.0D).normal(-1.0F, 0.0F, 0.0F).endVertex();
				buffer.pos(lowerU, upperV, -0.5D).tex(0.751953125D, 0.0D).normal(1.0F, 0.0F, 0.0F).endVertex();
				buffer.pos(lowerU, lowerV, -0.5D).tex(0.751953125D, 0.0625D).normal(1.0F, 0.0F, 0.0F).endVertex();
				buffer.pos(lowerU, lowerV, 0.5D).tex(0.751953125D, 0.0625D).normal(1.0F, 0.0F, 0.0F).endVertex();
				buffer.pos(lowerU, upperV, 0.5D).tex(0.751953125D, 0.0D).normal(1.0F, 0.0F, 0.0F).endVertex();
				tessellator.draw();
			}
		}
	}
	
	private void setLightmap(T painting, float u, float v)
	{
		int x = MathHelper.floor(painting.posX);
		int y = MathHelper.floor(painting.posY + (double)(v / 16.0F));
		int z = MathHelper.floor(painting.posZ);
		Direction enumfacing = painting.getFacingDirection();
		
		if (enumfacing == Direction.NORTH)
			x = MathHelper.floor(painting.posX + (double)(u / 16.0F));
		
		if (enumfacing == Direction.WEST)
			z = MathHelper.floor(painting.posZ - (double)(u / 16.0F));
		
		if (enumfacing == Direction.SOUTH)
			x = MathHelper.floor(painting.posX - (double)(u / 16.0F));
		
		if (enumfacing == Direction.EAST)
			z = MathHelper.floor(painting.posZ + (double)(u / 16.0F));
		
		int l = this.renderManager.world.getCombinedLight(new BlockPos(x, y, z), 0);
		int s = l % 65536;
		int t = l / 65536;
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float)s, (float)t);
		GlStateManager.color3f(1.0F, 1.0F, 1.0F);
	}
}
