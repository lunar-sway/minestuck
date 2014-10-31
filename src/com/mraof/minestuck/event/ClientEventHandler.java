package com.mraof.minestuck.event;

import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.inventory.ContainerEditmode;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

/**
 * Used to track mixed client sided events.
 */
public class ClientEventHandler {
	
	@SubscribeEvent
	public void onConnectedToServer(ClientConnectedToServerEvent event) {
		GuiPlayerStats.normalTab = GuiPlayerStats.NormalGuiType.CAPTCHA_DECK;
		GuiPlayerStats.editmodeTab = GuiPlayerStats.EditmodeGuiType.DEPLOY_LIST;
//		ContainerEditmode.scrollIndex = 0;
	}
	
}
