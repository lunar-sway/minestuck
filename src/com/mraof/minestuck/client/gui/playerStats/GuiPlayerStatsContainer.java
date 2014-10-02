package com.mraof.minestuck.client.gui.playerStats;

import static com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.*;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class GuiPlayerStatsContainer extends GuiContainer {
	
	protected int guiWidth, guiHeight;
	protected int xOffset, yOffset;
	
	private boolean mode;
	
	public GuiPlayerStatsContainer(Container container, boolean mode) {
		super(container);
		this.mode = mode;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		xOffset = (width-guiWidth)/2;
		yOffset = (height-guiHeight+tabHeight-tabOverlap)/2;
	}
	
}
