package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.item.EntityHangingArt;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHangingArt<T extends EntityHangingArt> extends Render<T>
{
	private final ResourceLocation ART_TEXTURE;
	
	public RenderHangingArt(RenderManager renderManagerIn, String artPath)
	{
		super(renderManagerIn);
		ART_TEXTURE = new ResourceLocation("minestuck:textures/paintings/" + artPath + ".png");
	}
	
	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.enableRescaleNormal();
		this.bindEntityTexture(entity);
		EntityHangingArt.IArt art = entity.art;
		float f = 0.0625F;
		GlStateManager.scale(0.0625F, 0.0625F, 0.0625F);
		
		if (this.renderOutlines)
		{
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}
		
		this.renderPainting(entity, art.getSizeX(), art.getSizeY(), art.getOffsetX(), art.getOffsetY());
		
		if (this.renderOutlines)
		{
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}
		
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityHangingArt entity)
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
				VertexBuffer vertexbuffer = tessellator.getBuffer();
				vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
				vertexbuffer.pos((double)upperU, (double)lowerV, -0.5D).tex((double)f20, (double)f21).normal(0.0F, 0.0F, -1.0F).endVertex();
				vertexbuffer.pos((double)lowerU, (double)lowerV, -0.5D).tex((double)f19, (double)f21).normal(0.0F, 0.0F, -1.0F).endVertex();
				vertexbuffer.pos((double)lowerU, (double)upperV, -0.5D).tex((double)f19, (double)f22).normal(0.0F, 0.0F, -1.0F).endVertex();
				vertexbuffer.pos((double)upperU, (double)upperV, -0.5D).tex((double)f20, (double)f22).normal(0.0F, 0.0F, -1.0F).endVertex();
				vertexbuffer.pos((double)upperU, (double)upperV, 0.5D).tex(0.75D, 0.0D).normal(0.0F, 0.0F, 1.0F).endVertex();
				vertexbuffer.pos((double)lowerU, (double)upperV, 0.5D).tex(0.8125D, 0.0D).normal(0.0F, 0.0F, 1.0F).endVertex();
				vertexbuffer.pos((double)lowerU, (double)lowerV, 0.5D).tex(0.8125D, 0.0625D).normal(0.0F, 0.0F, 1.0F).endVertex();
				vertexbuffer.pos((double)upperU, (double)lowerV, 0.5D).tex(0.75D, 0.0625D).normal(0.0F, 0.0F, 1.0F).endVertex();
				vertexbuffer.pos((double)upperU, (double)upperV, -0.5D).tex(0.75D, 0.001953125D).normal(0.0F, 1.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)lowerU, (double)upperV, -0.5D).tex(0.8125D, 0.001953125D).normal(0.0F, 1.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)lowerU, (double)upperV, 0.5D).tex(0.8125D, 0.001953125D).normal(0.0F, 1.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)upperU, (double)upperV, 0.5D).tex(0.75D, 0.001953125D).normal(0.0F, 1.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)upperU, (double)lowerV, 0.5D).tex(0.75D, 0.001953125D).normal(0.0F, -1.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)lowerU, (double)lowerV, 0.5D).tex(0.8125D, 0.001953125D).normal(0.0F, -1.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)lowerU, (double)lowerV, -0.5D).tex(0.8125D, 0.001953125D).normal(0.0F, -1.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)upperU, (double)lowerV, -0.5D).tex(0.75D, 0.001953125D).normal(0.0F, -1.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)upperU, (double)upperV, 0.5D).tex(0.751953125D, 0.0D).normal(-1.0F, 0.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)upperU, (double)lowerV, 0.5D).tex(0.751953125D, 0.0625D).normal(-1.0F, 0.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)upperU, (double)lowerV, -0.5D).tex(0.751953125D, 0.0625D).normal(-1.0F, 0.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)upperU, (double)upperV, -0.5D).tex(0.751953125D, 0.0D).normal(-1.0F, 0.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)lowerU, (double)upperV, -0.5D).tex(0.751953125D, 0.0D).normal(1.0F, 0.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)lowerU, (double)lowerV, -0.5D).tex(0.751953125D, 0.0625D).normal(1.0F, 0.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)lowerU, (double)lowerV, 0.5D).tex(0.751953125D, 0.0625D).normal(1.0F, 0.0F, 0.0F).endVertex();
				vertexbuffer.pos((double)lowerU, (double)upperV, 0.5D).tex(0.751953125D, 0.0D).normal(1.0F, 0.0F, 0.0F).endVertex();
				tessellator.draw();
			}
		}
	}
	
	private void setLightmap(T painting, float u, float v)
	{
		int x = MathHelper.floor(painting.posX);
		int y = MathHelper.floor(painting.posY + (double)(v / 16.0F));
		int z = MathHelper.floor(painting.posZ);
		EnumFacing enumfacing = painting.facingDirection;
		
		if (enumfacing == EnumFacing.NORTH)
			x = MathHelper.floor(painting.posX + (double)(u / 16.0F));
		
		if (enumfacing == EnumFacing.WEST)
			z = MathHelper.floor(painting.posZ - (double)(u / 16.0F));
		
		if (enumfacing == EnumFacing.SOUTH)
			x = MathHelper.floor(painting.posX - (double)(u / 16.0F));
		
		if (enumfacing == EnumFacing.EAST)
			z = MathHelper.floor(painting.posZ + (double)(u / 16.0F));
		
		int l = this.renderManager.world.getCombinedLight(new BlockPos(x, y, z), 0);
		int s = l % 65536;
		int t = l / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)s, (float)t);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
	}
}
