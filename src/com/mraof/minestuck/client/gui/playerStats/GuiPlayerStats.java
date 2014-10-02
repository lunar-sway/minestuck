package com.mraof.minestuck.client.gui.playerStats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public abstract class GuiPlayerStats extends GuiScreen {
	
	static final ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	static final Class<? extends GuiScreen>[] normalGuis = new Class[]{TempGuiPlayerStats.class};
	static final Class<? extends GuiScreen>[] editmodeGuis = new Class[]{TempGuiPlayerStats.class};
	
	static final int tabWidth = 28, tabHeight = 32, tabOverlap = 4;
	
	/**
	 * Integers indicating which tab that was last opened.
	 */
	public static int normalTab, editmodeTab;
	
	public Minecraft mc;
	
	protected int guiWidth, guiHeight;
	protected int xOffset, yOffset;
	
	private boolean mode;
	
	public GuiPlayerStats(boolean mode) {
		this.mode = mode;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		xOffset = (width-guiWidth)/2;
		yOffset = (height-guiHeight+tabHeight-tabOverlap)/2;
		mc = Minecraft.getMinecraft();
	}
	
	protected void drawTabs() {
		mc.getTextureManager().bindTexture(icons);
		
		if(mode)
			for(int i = 0; i < normalGuis.length; i++) {
				if(normalTab != i)
					drawTexturedModalRect(xOffset+i*(tabWidth+2),yOffset-tabHeight+tabOverlap,i==0?0:tabWidth,0, tabWidth, tabHeight);
			}
		else
			for(int i = 0; i < editmodeGuis.length; i++) {
				if(editmodeTab != i)
					drawTexturedModalRect(xOffset+i*(tabWidth+2),yOffset-tabHeight+tabOverlap,i==0?0:tabWidth,0, tabWidth, tabHeight);
			}
	}
	
	protected void drawActivatedTab() {
		mc.getTextureManager().bindTexture(icons);
		
		if(mode)
			drawTexturedModalRect(xOffset+normalTab*(tabWidth+2),yOffset-tabHeight+tabOverlap,normalTab==0?0:tabWidth,tabHeight, tabWidth, tabHeight);
		else
			drawTexturedModalRect(xOffset+editmodeTab*(tabWidth+2),yOffset-tabHeight+tabOverlap,editmodeTab==0?0:tabWidth,tabHeight, tabWidth, tabHeight);
	}
	
}
