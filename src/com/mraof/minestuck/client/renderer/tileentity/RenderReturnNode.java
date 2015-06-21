package com.mraof.minestuck.client.renderer.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderReturnNode extends TileEntitySpecialRenderer
{
	
	private static final ResourceLocation inner = new ResourceLocation("minestuck","textures/blocks/NodeSpiroInner.png");
	private static final ResourceLocation outer = new ResourceLocation("minestuck","textures/blocks/NodeSpiroOuter.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float f, int p_180535_9_)
	{
		GlStateManager.pushMatrix();
		GlStateManager.color(0.5F, 0.5F, 1.0F);
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();
		float tick = tileEntity.getWorld().getTotalWorldTime() + f;
		GlStateManager.translate(posX, posY, posZ);
		
		GlStateManager.pushMatrix();
		GlStateManager.rotate(tick, 0, 1, 0);
		double y = 0.5;// + MathHelper.sin(tick)*0.1;
		this.bindTexture(inner);
		renderer.startDrawingQuads();
		renderer.addVertexWithUV(-1, y, -1, 0, 0);
		renderer.addVertexWithUV(-1, y, 1, 0, 1);
		renderer.addVertexWithUV(1, y, 1, 1, 1);
		renderer.addVertexWithUV(1, y, -1, 1, 0);
		Tessellator.getInstance().draw();
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.rotate(-tick/1.5F, 0, 1, 0);
		y = 0.5 + MathHelper.sin(tick/50)*0.1;
		this.bindTexture(outer);
		renderer.startDrawingQuads();
		renderer.addVertexWithUV(-1, y, -1, 0, 0);
		renderer.addVertexWithUV(-1, y, 1, 0, 1);
		renderer.addVertexWithUV(1, y, 1, 1, 1);
		renderer.addVertexWithUV(1, y, -1, 1, 0);
		Tessellator.getInstance().draw();
		GlStateManager.popMatrix();
		
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
	
}