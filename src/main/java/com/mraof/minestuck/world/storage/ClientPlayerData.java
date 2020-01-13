package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.network.GristCachePacket;
import com.mraof.minestuck.util.Title;

/**
 * Contains static field for any {@link PlayerData} fields that also need client access.
 * @author kirderf1
 */
public class ClientPlayerData
{
	public static Title title;
	public static int rung;
	public static float rungProgress;
	public static long boondollars;
	private static GristSet playerGrist;
	private static GristSet targetGrist;
	
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