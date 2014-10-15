package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.inventory.ContainerInvEditmode;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiInventoryEditmode extends GuiPlayerStatsContainer {

	private ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/guiInvEditmode.png");
	private ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	private static final int leftArrowX = 7, rightArrowX = 151, arrowY = 23;
	
	public GuiInventoryEditmode() {
		super(new ContainerInvEditmode(GuiPlayerStats.editmodeTab == 0), false);
		guiWidth = 176;
		guiHeight = 98;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int xcor, int ycor) {
		drawTabs();
		
		mc.getTextureManager().bindTexture(guiBackground);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		this.drawTexturedModalRect(xOffset+leftArrowX, yOffset+arrowY, guiWidth, 18, 18, 18);
		this.drawTexturedModalRect(xOffset+rightArrowX, yOffset+arrowY, guiWidth+18, 18, 18, 18);
		
		drawActiveTabAndIcons();
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int xcor, int ycor) {
		drawTabTooltip(xcor, ycor);
	}
	
}
