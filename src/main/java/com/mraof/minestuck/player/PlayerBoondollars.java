package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.BoondollarDataPacket;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlayerBoondollars
{
	public static long getBoondollars(PlayerData playerData)
	{
		return playerData.getData(MSAttachments.BOONDOLLARS);
	}
	
	public static void addBoondollars(PlayerData playerData, long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Boondollar amount may not be negative.");
		
		setBoondollars(playerData, getBoondollars(playerData) + amount);
	}
	
	public static void takeBoondollars(PlayerData playerData, long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Boondollar amount may not be negative.");
		
		setBoondollars(playerData, getBoondollars(playerData) - amount);
	}
	
	public static boolean tryTakeBoondollars(PlayerData playerData, long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Boondollar amount may not be negative.");
		
		long newAmount = getBoondollars(playerData) - amount;
		
		if(newAmount < 0)
			return false;
		
		setBoondollars(playerData, newAmount);
		return true;
	}
	
	public static void setBoondollars(PlayerData playerData, long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Boondollar amount may not be negative.");
		
		if(amount != getBoondollars(playerData))
		{
			playerData.setData(MSAttachments.BOONDOLLARS, amount);
			sendBoondollars(playerData.getPlayer(), playerData);
		}
	}
	
	@SubscribeEvent
	private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		
		PlayerData playerData = PlayerData.get(player).orElseThrow();
		PlayerBoondollars.sendBoondollars(player, playerData);
	}
	
	private static void sendBoondollars(ServerPlayer player, PlayerData playerData)
	{
		if(player == null)
			return;
		BoondollarDataPacket packet = BoondollarDataPacket.create(getBoondollars(playerData));
		player.connection.send(packet);
	}
}
