package com.mraof.minestuck.client.renderer.tileentity;

import com.mraof.minestuck.tileentity.GateTileEntity;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderGate extends TileEntityRenderer<GateTileEntity>
{
	
	private static final ResourceLocation nodeInner = new ResourceLocation("minestuck","textures/blocks/node_spiro_inner.png");
	private static final ResourceLocation nodeOuter = new ResourceLocation("minestuck","textures/blocks/node_spiro_outer.png");
	
	@Override
	public void render(GateTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage)
	{
		int color;
		if(tileEntityIn.colorIndex == -1)
			color = 0x0057FF;
		else color = ColorCollector.getColor(tileEntityIn.colorIndex);
		float r = ((color >> 16) & 255)/255F;
		float g = ((color >> 8) & 255)/255F;
		float b = (color & 255)/255F;
		
		GlStateManager.pushLightingAttrib();
		GlStateManager.color3f(r, g, b);
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		
		if(tileEntityIn.isGate())
			renderGateAt(tileEntityIn, x, y, z, partialTicks, destroyStage);
		else renderReturnNodeAt(tileEntityIn, x, y, z, partialTicks, destroyStage);
		
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
	
	public void renderGateAt(GateTileEntity tileEntity, double posX, double posY, double posZ, float f, int p_180535_9_)
	{
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		float tick = tileEntity.getWorld().getGameTime() + f;
		GlStateManager.translated(posX + 0.5, posY, posZ + 0.5);
		
		GlStateManager.pushMatrix();
		GlStateManager.rotatef(tick, 0, 1, 0);
		double y = 0.5;
		this.bindTexture(nodeInner);
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(-1.5, y, -1.5).tex(0, 0).endVertex();
		buffer.pos(-1.5, y, 1.5).tex(0, 1).endVertex();
		buffer.pos(1.5, y, 1.5).tex(1, 1).endVertex();
		buffer.pos(1.5, y, -1.5).tex(1, 0).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.popMatrix();
	}
	
	public void renderReturnNodeAt(GateTileEntity tileEntity, double posX, double posY, double posZ, float f, int p_180535_9_)
	{
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		float tick = tileEntity.getWorld().getGameTime() + f;
		GlStateManager.translated(posX, posY, posZ);
		
		GlStateManager.pushMatrix();
		GlStateManager.rotatef(tick, 0, 1, 0);
		double y = 0.5;
		this.bindTexture(nodeInner);
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(-1, y, -1).tex(0, 0).endVertex();
		buffer.pos(-1, y, 1).tex(0, 1).endVertex();
		buffer.pos(1, y, 1).tex(1, 1).endVertex();
		buffer.pos(1, y, -1).tex(1, 0).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.rotatef(-tick/1.5F, 0, 1, 0);
		y = 0.5 + MathHelper.sin(tick/50)*0.1;
		this.bindTexture(nodeOuter);
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(-1, y, -1).tex(0, 0).endVertex();
		buffer.pos(-1, y, 1).tex(0, 1).endVertex();
		buffer.pos(1, y, 1).tex(1, 1).endVertex();
		buffer.pos(1, y, -1).tex(1, 0).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.popMatrix();
	}
	
}