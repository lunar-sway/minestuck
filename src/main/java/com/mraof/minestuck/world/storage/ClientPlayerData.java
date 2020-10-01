package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.network.data.GristCachePacket;
import com.mraof.minestuck.player.Title;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Contains static field for any {@link PlayerData} fields that also need client access.
 * @author kirderf1
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientPlayerData	//TODO reduce visibility of fields
{
	public static Modus clientSideModus;
	public static Title title;
	public static boolean echeladderAvailable;
	public static int rung;
	public static float rungProgress;
	public static long boondollars;
	private static GristSet playerGrist;
	private static GristSet targetGrist;
	public static int playerColor;
	public static boolean displaySelectionGui;
	public static boolean dataCheckerAccess;
	
	@SubscribeEvent
	public static void onLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event)
	{
		clientSideModus = null;
		title = null;
		rung = -1;
		playerColor = -1;
		displaySelectionGui = false;
	}
	
	public static void onPacketReceived(GristCachePacket packet)
	{
		if (packet.isEditmode)
		{
			targetGrist = packet.gristCache;
		}
		else
		{
			playerGrist = packet.gristCache;
		}
	}
	
	public static GristSet getClientGrist()
	{
		return ClientEditHandler.isActive() ? targetGrist : playerGrist;
	}
}