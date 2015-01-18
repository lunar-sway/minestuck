package com.mraof.minestuck.client.gui.playerStats;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.KindAbstratusType;

public class GuiStrifeSpecibus extends GuiPlayerStats
{
	
	private static final ResourceLocation guiStrifeSelector = new ResourceLocation("minestuck", "textures/gui/StrifeSelector.png");
	
	private static final int columnWidth = 70, columns = 3;
	
	public GuiStrifeSpecibus()
	{
		super();
		guiWidth = 228;
		guiHeight = 150;
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3) {
		super.drawScreen(xcor, ycor, par3);
		this.drawDefaultBackground();
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		drawTabs();
		
		this.mc.getTextureManager().bindTexture(guiStrifeSelector);
		
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		String message = "This feature isn't implemented yet.";//StatCollector.translateToLocal("gui.kindAbstrata.name");
		mc.fontRenderer.drawString(message, (this.width / 2) - mc.fontRenderer.getStringWidth(message) / 2, yOffset + 12, 0x404040);
		
		int i = 0;
		for(KindAbstratusType type : KindAbstratusList.getTypeList()) {
			String typeName = type.getDisplayName().toLowerCase();
			int xPos = xOffset+9+(columnWidth)*((i%columns)+1)-mc.fontRenderer.getStringWidth(typeName);
			int yPos = yOffset+35+(mc.fontRenderer.FONT_HEIGHT+1)*(int)(i/columns);
			
			if(!isPointInRegion(xOffset+9+(columnWidth)*(i%columns)+1, yPos-1, columnWidth-1, mc.fontRenderer.FONT_HEIGHT+1, xcor, ycor))
				mc.fontRenderer.drawString(typeName, xPos, yPos, 0xFFFFFF, false);
			else {
				drawRect(xOffset+9+(columnWidth)*(i%columns)+1, yPos-1, xOffset+9+(columnWidth)*((i%columns)+1), yPos+mc.fontRenderer.FONT_HEIGHT, 0xFFAFAFAF);
				mc.fontRenderer.drawString(typeName, xPos, yPos, 0x000000, false);
			}
			i++;
		}
		
		drawActiveTabAndOther(xcor, ycor);
		
	}
	
}
