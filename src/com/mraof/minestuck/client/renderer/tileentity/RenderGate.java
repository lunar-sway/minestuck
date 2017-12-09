package com.mraof.minestuck.client.renderer.tileentity;

import com.mraof.minestuck.tileentity.TileEntityGate;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderGate extends TileEntitySpecialRenderer<TileEntityGate>
{
	
	private static final ResourceLocation nodeInner = new ResourceLocation("minestuck","textures/blocks/node_spiro_inner.png");
	private static final ResourceLocation nodeOuter = new ResourceLocation("minestuck","textures/blocks/node_spiro_outer.png");
	
	@Override
	public void render(TileEntityGate te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		int color;
		if(te.colorIndex == -1)
			color = 0x0057FF;
		else color = ColorCollector.getColor(te.colorIndex);
		float r = ((color >> 16) & 255)/255F;
		float g = ((color >> 8) & 255)/255F;
		float b = (color & 255)/255F;
		
		GlStateManager.pushAttrib();
		GlStateManager.color(r, g, b);
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		
		if(te.isGate())
			renderGateAt(te, x, y, z, partialTicks, destroyStage);
		else renderReturnNodeAt(te, x, y, z, partialTicks, destroyStage);
		
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
	
	public void renderGateAt(TileEntityGate tileEntity, double posX, double posY, double posZ, float f, int p_180535_9_)
	{
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		float tick = tileEntity.getWorld().getTotalWorldTime() + f;
		GlStateManager.translate(posX + 0.5, posY, posZ + 0.5);
		
		GlStateManager.pushMatrix();
		GlStateManager.rotate(tick, 0, 1, 0);
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
	
	public void renderReturnNodeAt(TileEntityGate tileEntity, double posX, double posY, double posZ, float f, int p_180535_9_)
	{
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		float tick = tileEntity.getWorld().getTotalWorldTime() + f;
		GlStateManager.translate(posX, posY, posZ);
		
		GlStateManager.pushMatrix();
		GlStateManager.rotate(tick, 0, 1, 0);
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
		GlStateManager.rotate(-tick/1.5F, 0, 1, 0);
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