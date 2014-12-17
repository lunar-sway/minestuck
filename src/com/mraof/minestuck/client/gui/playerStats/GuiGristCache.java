package com.mraof.minestuck.client.gui.playerStats;

import java.util.Arrays;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;

public class GuiGristCache extends GuiPlayerStats
{
	
	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/GristCache.png");
	
	private static final int gristIconX = 21, gristIconY = 32;
	private static final int gristIconXOffset = 66, gristIconYOffset = 21;
	private static final int gristCountX = 44, gristCountY = 36;
	private static final int gristCountXOffset = 66, gristCountYOffset = 21;
	
	public GuiGristCache()
	{
		super();
		guiWidth = 226;
		guiHeight = 190;
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3) {
		super.drawScreen(xcor, ycor, par3);
		this.drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		drawTabs();
		
		this.mc.getTextureManager().bindTexture(guiGristcache);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		String cacheMessage = StatCollector.translateToLocal("gui.gristCache.name");
		mc.fontRendererObj.drawString(cacheMessage, (this.width / 2) - mc.fontRendererObj.getStringWidth(cacheMessage) / 2, yOffset + 12, 0x404040);
		
		drawActiveTabAndOther(xcor, ycor);
		
		GlStateManager.color(1,1,1);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		
		int tooltip = -1;
		
		for(int gristId = 0; gristId < GristType.allGrists; gristId++)
		{
			int row = (int) (gristId / 7);
			int column = (int) (gristId % 7);
			int gristXOffset = xOffset + gristIconX + (gristIconXOffset * row - row);
			int gristYOffset = yOffset + gristIconY + (gristIconYOffset * column - column);

			if(this.isPointInRegion(gristXOffset, gristYOffset, 16, 16, xcor, ycor)) {
				this.drawGradientRect(gristXOffset, gristYOffset, gristXOffset + 16, gristYOffset + 17, -2130706433, -2130706433);
				tooltip = gristId;
			}
			
			this.drawIcon(gristXOffset, gristYOffset, "textures/grist/" + GristType.values()[gristId].getName()+ ".png");
			mc.fontRendererObj.drawString(Integer.toString(MinestuckPlayerData.getClientGrist().getGrist(GristType.values()[gristId])),xOffset + gristCountX + (gristCountXOffset * row - row), yOffset + gristCountY + (gristCountYOffset * column - column), 0xddddee);
			
		}
		
		if (tooltip != -1)
		{
			drawHoveringText(Arrays.asList(StatCollector.translateToLocalFormatted("grist.format", GristType.values()[tooltip].getDisplayName())),
					xcor, ycor, fontRendererObj);
		}
	}
	
	private void drawIcon(int x,int y,String location) 
	{
		this.mc.getTextureManager().bindTexture(new ResourceLocation("minestuck",location));

		float scale = (float) 1/16;

		int iconX = 16;
		int iconY = 16;
		int iconU = 0;
		int iconV = 0;

		WorldRenderer render = Tessellator.getInstance().getWorldRenderer();
		render.startDrawingQuads();
		render.addVertexWithUV((double)(x + 0), (double)(y +  iconY), (double)this.zLevel, (double)((float)(iconU + 0) * scale), (double)((float)(iconV +  iconY) * scale));
		render.addVertexWithUV((double)(x + iconX), (double)(y +  iconY), (double)this.zLevel, (double)((float)(iconU + iconX) * scale), (double)((float)(iconV +  iconY) * scale));
		render.addVertexWithUV((double)(x + iconX), (double)(y + 0), (double)this.zLevel, (double)((float)(iconU + iconX) * scale), (double)((float)(iconV + 0) * scale));
		render.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(iconU + 0) * scale), (double)((float)(iconV + 0) * scale));
		Tessellator.getInstance().draw();
	}
	
}
