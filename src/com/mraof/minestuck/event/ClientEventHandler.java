package com.mraof.minestuck.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.client.gui.GuiColorSelector;
import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.inventory.ContainerEditmode;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.util.ColorCollector;
import com.mraof.minestuck.util.MinestuckPlayerData;

/**
 * Used to track mixed client sided events.
 */
@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
	
	@SubscribeEvent
	public void onConnectedToServer(ClientConnectedToServerEvent event)
	{
		GuiPlayerStats.normalTab = GuiPlayerStats.NormalGuiType.CAPTCHA_DECK;
		GuiPlayerStats.editmodeTab = GuiPlayerStats.EditmodeGuiType.DEPLOY_LIST;
		ContainerEditmode.clientScroll = 0;
		CaptchaDeckHandler.clientSideModus = null;
		MinestuckPlayerData.title = null;
		ColorCollector.playerColor = -1;
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.START)
		{
			if(ColorCollector.selectionGui && Minecraft.getMinecraft().currentScreen == null)
			{
				ColorCollector.selectionGui = false;
				Minecraft.getMinecraft().displayGuiScreen(new GuiColorSelector());
			}
			
		}
	}
	
}
