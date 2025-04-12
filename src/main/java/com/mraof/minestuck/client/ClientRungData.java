package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.RungDisplayDataPacket;
import com.mraof.minestuck.player.Rung;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

import java.util.Collections;
import java.util.List;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ClientRungData
{
	private static List<Rung.DisplayData> RUNG_LIST = Collections.emptyList();
	
	public static List<Rung.DisplayData> getRungList()
	{
		return RUNG_LIST;
	}
	
	public static int getFinalRung()
	{
		return getRungList().size() - 1;
	}
	
	public static int textColor(int rung)
	{
		return rung < getRungList().size() ? getRungList().get(rung).textColor() : 0xFFFFFF;
	}
	
	public static int backgroundColor(int rung, int textColor)
	{
		return rung < getRungList().size() ? getRungList().get(rung).backgroundColor() : ~textColor;
	}
	
	public static long getGristCapacity(int rung)
	{
		return rung < getRungList().size() ? getRungList().get(rung).gristCapacity() : 0;
	}
	
	public static void onPacket(RungDisplayDataPacket packet)
	{
		RUNG_LIST = packet.rungList();
	}
	
	@SubscribeEvent
	private static void onLogout(ClientPlayerNetworkEvent.LoggingOut event)
	{
		RUNG_LIST = Collections.emptyList();
	}
}
