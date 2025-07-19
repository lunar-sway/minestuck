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
import java.util.Optional;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ClientRungData
{
	private static final Rung.DisplayData DUMMY_DATA = new Rung.DisplayData(0xFFFFFF, 0x000000, 0, Optional.empty(),
			new Rung.DisplayAttributes(0D, 0D, 1D, 1D));
	private static List<Rung.DisplayData> RUNG_LIST = Collections.emptyList();
	
	public static int getFinalRungIndex()
	{
		return RUNG_LIST.size() - 1;
	}
	
	public static Rung.DisplayData getData(int rungIndex)
	{
		return rungIndex < RUNG_LIST.size() ? RUNG_LIST.get(rungIndex) : DUMMY_DATA;
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
