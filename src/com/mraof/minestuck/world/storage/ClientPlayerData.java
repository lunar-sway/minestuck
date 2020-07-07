package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.inventory.specibus.StrifeSpecibus;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.network.GristCachePacket;
import com.mraof.minestuck.util.Title;

import java.util.List;

public class ClientPlayerData
{
	public static Title title;
	public static int rung;
	public static float rungProgress;
	public static long boondollars;
	public static List<StrifeSpecibus> playerPortfolio;
	static GristSet playerGrist;
	static GristSet targetGrist;
	
	public static void onPacketRecived(GristCachePacket packet)
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