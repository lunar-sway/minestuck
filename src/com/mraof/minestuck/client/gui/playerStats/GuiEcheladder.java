package com.mraof.minestuck.client.gui.playerStats;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;

public class GuiEcheladder extends GuiPlayerStats
{
	
	private static final ResourceLocation guiEcheladder = new ResourceLocation("minestuck", "textures/gui/echeladder.png");
	
	private static final int ladderXOffset = 163, ladderYOffset = 25;
	private static final int rows = 12;
	private int scrollIndex;
	
	public GuiEcheladder()
	{
		super();
		guiWidth = 256;
		guiHeight = 212;
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3) {
		super.drawScreen(xcor, ycor, par3);
		this.drawDefaultBackground();
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		drawTabs();
		
		if(scrollIndex == 13)
			scrollIndex = 0;
		else scrollIndex++;
		this.mc.getTextureManager().bindTexture(guiEcheladder);
		int index = scrollIndex % 14;
		for(int i = 0; i < rows; i++)
			drawTexturedModalRect(xOffset+90,yOffset+38-index+(i*14),0,212,146,14);
		for(int i = 0; i < rows; i++) {
			String s = "MethodOfScrollIndexAndI";
			mc.fontRenderer.drawString(s, xOffset+ladderXOffset - mc.fontRenderer.getStringWidth(s) / 2, yOffset+40-index+(i*14), 0xFFFFFF);
		}
		GL11.glColor3f(1,1,1);
		
		this.mc.getTextureManager().bindTexture(guiEcheladder);
		
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		drawActiveTabAndOther(xcor, ycor);
		
	}
	
}
