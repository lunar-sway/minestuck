package com.mraof.minestuck.client.gui.playerStats;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiInventoryEditmode extends GuiPlayerStatsContainer {

	private ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/guiInvEditmode.png");
	private ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	public GuiInventoryEditmode(EntityPlayer player) {
		super(player.inventoryContainer, false);
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		
		
	}
	
}
