package com.mraof.minestuck.client.gui.playerStats;

import java.util.Random;

import com.mraof.minestuck.util.Echeladder;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiEcheladder extends GuiPlayerStats
{
	
	private static final ResourceLocation guiEcheladder = new ResourceLocation("minestuck", "textures/gui/echeladder.png");
	
	private static final int[] backgrounds = new int[] {};
	private static final int[] textColors = new int[] {};
	
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
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		drawTabs();
		
		this.mc.getTextureManager().bindTexture(guiEcheladder);
		int index = scrollIndex % 14;
		for(int i = 0; i < rows; i++)
			drawTexturedModalRect(xOffset + 90, yOffset + 21 - index + i*14, 0, 212, 146, 14);
		
		Random rand = new Random(452619373);
		for(int i = 0; i < rows; i++)
		{
			int y = yOffset + 177 - index - i*14;
			int rug = scrollIndex/14 + i;
			if(rug > Echeladder.RUG_COUNT)
				break;
			
			int textColor = 0xFFFFFF;
			if(rug <= MinestuckPlayerData.rug)
			{
				textColor = textColors.length > rug ? textColors[rug] : rand.nextInt(0xFFFFFF);
				drawRect(xOffset + 90, y, xOffset + 236, y + 12, backgrounds.length > rug ? backgrounds[rug] : (textColor^0xFFFFFFFF));
			}
			
			String s = StatCollector.canTranslate("echeladder.rug"+rug) ? StatCollector.translateToLocal("echeladder.rug"+rug) : "Rug "+(rug+1);
			mc.fontRendererObj.drawString(s, xOffset+ladderXOffset - mc.fontRendererObj.getStringWidth(s) / 2, y + 2, textColor);
		}
		GlStateManager.color(1,1,1);
		
		this.mc.getTextureManager().bindTexture(guiEcheladder);
		
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		drawActiveTabAndOther(xcor, ycor);
		
	}
	
}
