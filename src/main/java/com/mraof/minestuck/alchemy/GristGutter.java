package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SessionHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

/**
 * A class that handles Grist overflow whenever you acquire too much grist.
 * @author Doro
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class GristGutter
{
	public static final int GUTTER_CAPACITY = 10000;
	
	private final NonNegativeGristSet gristSet = new NonNegativeGristSet();
	private long gristTotal = 0;
	
	public ImmutableGristSet getCache()
	{
		return gristSet.asImmutable();
	}
	
	public long getRemainingCapacity(Session session, PlayerSavedData playerSavedData)
	{
		return (long) (GUTTER_CAPACITY * gutterMultiplierForSession(session, playerSavedData)) - gristTotal;
	}
	
	public static double gutterMultiplierForSession(Session session, PlayerSavedData playerSavedData)
	{
		double gutterMultiplier = 0;
		for(PlayerIdentifier player : session.getPlayerList())
		{
			PlayerData data = playerSavedData.getData(player);
			gutterMultiplier += data.getGutterMultipler();
		}
		return gutterMultiplier;
	}
	
	public void addGristFrom(GristSet set, Session session, PlayerSavedData playerSavedData)
	{
		for(GristAmount amount : set.getAmounts())
		{
			GristType type = amount.getType();
			long maximumAllowed = getRemainingCapacity(session, playerSavedData);
			
			if(maximumAllowed <= 0)
				return;
			
			long amountToAdd = Math.min(maximumAllowed, amount.getAmount());
			set.addGrist(type, -amountToAdd);
			this.addGristInternal(type, amountToAdd);
		}
	}
	
	/**
	 * The grist set is currently only modified here,
	 * which lets us be certain that gutterTotal is accurate.
	 */
	private void addGristInternal(GristType type, long amount)
	{
		this.gristSet.addGrist(type, amount);
		this.gristTotal += amount;
	}
	
	@SubscribeEvent
	public static void onServerTickEvent(TickEvent.ServerTickEvent event)
	{
		//noinspection resource
		if(event.getServer().overworld().getGameTime() % 200 == 0)
		{
			for(Session session : SessionHandler.get(event.getServer()).getSessions())
			{
				session.getGristGutter().distributeToPlayers(session.getPlayerList(), event.getServer());
			}
		}
	}
	
	private void distributeToPlayers(Set<PlayerIdentifier> players, MinecraftServer server)
	{
		// TODO iterate in a random order, so that no player gets priority
		for(PlayerIdentifier player : players)
		{
			tickDistributionToPlayer(player, server);
		}
	}
	
	private void tickDistributionToPlayer(PlayerIdentifier player, MinecraftServer server)
	{
		PlayerData data = PlayerSavedData.getData(player, server);
		
		long spliceAmount = (long) (data.getEcheladder().getGristCapacity() * getDistributionRateModifier());
		
		NonNegativeGristSet capacity = GristHelper.getCapacitySet(data);
		GristSet gristToTransfer = this.takeWithinCapacity(spliceAmount, capacity);
		GristSet remainder = GristHelper.increaseAndReturnExcess(data, gristToTransfer);
		if(!remainder.isEmpty())
			throw new IllegalStateException("Took more grist than could be given to the player. Got back grist: " + remainder);
	}
	
	private double getDistributionRateModifier()
	{
		return 1D/20D;
	}
	
	private GristSet takeWithinCapacity(long amount, NonNegativeGristSet capacity)
	{
		long remaining = amount;
		GristSet takenGrist = new GristSet();
		//TODO randomize iteration order
		for(GristAmount capacityAmount : capacity.getAmounts())
		{
			GristType type = capacityAmount.getType();
			long amountInGutter = this.gristSet.getGrist(type);
			if(amountInGutter > 0)
			{
				long takenAmount = Math.min(remaining, Math.min(capacityAmount.getAmount(), amountInGutter));
				this.addGristInternal(type, -takenAmount);
				takenGrist.addGrist(type, takenAmount);
				remaining -= takenAmount;
				if(remaining <= 0)
					break;
			}
		}
		return takenGrist;
	}
}