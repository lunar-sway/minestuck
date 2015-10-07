package com.mraof.minestuck.event;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ServerEventHandler
{
	
	public static long lastDay;
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			
			if(!MinestuckConfig.hardMode && event.world.provider.getDimensionId() == 0)
			{
				long time = event.world.getWorldTime() / 24000L;
				if(time != lastDay)
				{
					lastDay = time;
					SkaianetHandler.resetGivenItems();
				}
			}
		}
	}
}