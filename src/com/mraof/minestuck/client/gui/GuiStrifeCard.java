package com.mraof.minestuck.client.gui;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.KindAbstratusType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiStrifeCard extends GuiScreenMinestuck
{
	private static int guiWidth = 147, guiHeight = 185;
	private static int xOffset, yOffset;
	private static final ResourceLocation guiStrifeSelector = new ResourceLocation("minestuck", "textures/gui/strife_specibus/strife_selector.png");
	
	private static final int columnWidth = 70, columns = 3;
	
	@Override
	public void initGui() 
	{
		super.initGui();
		xOffset = (width-guiWidth)/2;
		yOffset = (height-guiHeight)/2;
		mc = Minecraft.getMinecraft();
	}
	
	@Override
	public boolean doesGuiPauseGame() 
	{
		return false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		//this.drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(guiStrifeSelector);
		
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		
		int i = 0;
		for(KindAbstratusType type : KindAbstratusList.getTypeList()) {
			String typeName = type.getDisplayName().toLowerCase();
			int xPos = xOffset+9+(columnWidth)*((i%columns)+1)-mc.fontRenderer.getStringWidth(typeName);
			int yPos = yOffset+35+(mc.fontRenderer.FONT_HEIGHT+1)*(int)(i/columns);
			
			if(!isPointInRegion(xOffset+9+(columnWidth)*(i%columns)+1, yPos-1, columnWidth-1, mc.fontRenderer.FONT_HEIGHT+1, mouseX, mouseY))
				mc.fontRenderer.drawString(typeName, xPos, yPos, 0xFFFFFF);
			else {
				drawRect(xOffset+9+(columnWidth)*(i%columns)+1, yPos-1, xOffset+9+(columnWidth)*((i%columns)+1), yPos+mc.fontRenderer.FONT_HEIGHT, 0xFFAFAFAF);
				mc.fontRenderer.drawString(typeName, xPos, yPos, 0x000000);
			}
			i++;
		}
	}
}
