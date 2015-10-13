package com.mraof.minestuck.client.gui.playerStats;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiDataChecker extends GuiScreen
{
	
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/DataCheck.png");
	private static final int GUI_WIDTH = 207, GUI_HEIGHT = 132;
	
	
	@Override
	public void initGui()
	{
		for(int i = 0; i < 5; i++)
		{
			GuiButton button = new GuiButton(i, (width - GUI_WIDTH)/2 + 5, (height - GUI_HEIGHT)/2 + 17 + i*22, 180, 20, "");
			this.buttonList.add(button);
			button.enabled = false;
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		
		this.mc.getTextureManager().bindTexture(guiBackground);
		
		drawTexturedModalRect((width - GUI_WIDTH)/2, (height - GUI_HEIGHT)/2, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}