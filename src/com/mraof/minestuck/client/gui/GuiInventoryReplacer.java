package com.mraof.minestuck.client.gui;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldSettings.GameType;

public class GuiInventoryReplacer extends GuiInventory {
	
	public GuiInventoryReplacer(EntityPlayer player) {
		super(player);
	}
	
	@Override
	public void initGui() {
		boolean isFlying = this.mc.thePlayer.capabilities.isFlying;
		this.mc.playerController.setGameType(GameType.SURVIVAL);
		super.initGui();
		this.mc.playerController.setGameType(GameType.CREATIVE);
		this.mc.thePlayer.capabilities.isFlying = isFlying;
	}
	
	@Override
	public void updateScreen() {}
	
}
