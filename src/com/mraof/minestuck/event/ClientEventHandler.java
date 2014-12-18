package com.mraof.minestuck.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.inventory.ContainerEditmode;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;

/**
 * Used to track mixed client sided events.
 */
public class ClientEventHandler {
	
	@SubscribeEvent
	public void onConnectedToServer(ClientConnectedToServerEvent event)
	{
		GuiPlayerStats.normalTab = GuiPlayerStats.NormalGuiType.CAPTCHA_DECK;
		GuiPlayerStats.editmodeTab = GuiPlayerStats.EditmodeGuiType.DEPLOY_LIST;
		ContainerEditmode.clientScroll = 0;
		CaptchaDeckHandler.clientSideModus = null;
		MinestuckPlayerData.title = null;
	}
	
}
