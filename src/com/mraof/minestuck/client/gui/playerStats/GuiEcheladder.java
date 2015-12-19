package com.mraof.minestuck.client.gui.playerStats;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.input.Mouse;

import com.mraof.minestuck.util.Echeladder;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiEcheladder extends GuiPlayerStats
{
	
	private static final ResourceLocation guiEcheladder = new ResourceLocation("minestuck", "textures/gui/echeladder.png");
	
	private static final int MAX_SCROLL = Echeladder.RUNG_COUNT*14 - 154;
	private static final int[] backgrounds = new int[] {};
	private static final int[] textColors = new int[] {};
	
	private static final int ladderXOffset = 163, ladderYOffset = 25;
	private static final int rows = 12;
	private int scrollIndex;
	private boolean wasClicking, isScrolling;
	
	public GuiEcheladder()
	{
		super();
		guiWidth = 256;
		guiHeight = 212;
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3)
	{
		boolean mouseButtonDown = Mouse.isButtonDown(0);
		if(!wasClicking && mouseButtonDown && xcor >= xOffset + 80 && xcor < xOffset + 87 && ycor >= yOffset + 42 && ycor < yOffset + 186)
			isScrolling = true;
		else if(!mouseButtonDown)
			isScrolling = false;
		
		if(isScrolling)
		{
			scrollIndex = (int) (MAX_SCROLL*(ycor - yOffset - 179)/-131F);
			scrollIndex = MathHelper.clamp_int(scrollIndex, 0, MAX_SCROLL);
		}
		wasClicking = mouseButtonDown;
		
		super.drawScreen(xcor, ycor, par3);
		this.drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		drawTabs();
		
		this.mc.getTextureManager().bindTexture(guiEcheladder);
		int index = scrollIndex % 14;
		for(int i = 0; i < rows; i++)
			drawTexturedModalRect(xOffset + 90, yOffset + 175 + index - i*14, 0, 212, 146, 14);
		
		Random rand = new Random(452619373);
		for(int i = 0; i < scrollIndex/14; i++)
			rand.nextInt(0xFFFFFF);
		for(int i = 0; i < rows; i++)
		{
			int y = yOffset + 177 + index - i*14;
			int rung = scrollIndex/14 + i;
			if(rung > Echeladder.RUNG_COUNT)
				break;
			
			int textColor = 0xFFFFFF;
			if(rung <= MinestuckPlayerData.rung)
			{
				textColor = textColors.length > rung ? textColors[rung] : rand.nextInt(0xFFFFFF);
				drawRect(xOffset + 90, y, xOffset + 236, y + 12, backgrounds.length > rung ? backgrounds[rung] : (textColor^0xFFFFFFFF));
			} else rand.nextInt(0xFFFFFF);
			
			String s = StatCollector.canTranslate("echeladder.rung"+rung) ? StatCollector.translateToLocal("echeladder.rung"+rung) : "Rung "+(rung+1);
			mc.fontRendererObj.drawString(s, xOffset+ladderXOffset - mc.fontRendererObj.getStringWidth(s) / 2, y + 2, textColor);
		}
		GlStateManager.color(1,1,1);
		
		this.mc.getTextureManager().bindTexture(guiEcheladder);
		
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);

		this.drawTexturedModalRect(xOffset + 80, yOffset + 42 + (int) (131*(1 - scrollIndex/(float) MAX_SCROLL)), 0, 226, 7, 13);
		
		drawActiveTabAndOther(xcor, ycor);
		
	}
	
	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		
		if(i != 0)
		{
			if(i > 0)
				scrollIndex += 14;
			else scrollIndex -= 14;
			scrollIndex = MathHelper.clamp_int(scrollIndex, 0, MAX_SCROLL);
		}
	}
}