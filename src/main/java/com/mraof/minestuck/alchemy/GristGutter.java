package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SessionHandler;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;

/**
 * A class that handles Grist overflow whenever you acquire too much grist.
 * @author Doro
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class GristGutter
{
	public static final int GUTTER_CAPACITY = 10000;
	
	private final Session session;
	private final NonNegativeGristSet gristSet;
	private long gristTotal;
	
	public GristGutter(Session session)
	{
		this.session = session;
		this.gristSet = new NonNegativeGristSet();
		this.gristTotal = 0;
	}
	
	public GristGutter(Session session, ListTag listTag)
	{
		this.session = session;
		this.gristSet = NonNegativeGristSet.read(listTag);
		this.gristTotal = 0;
		for(GristAmount amount : this.gristSet.getAmounts())
			this.gristTotal += amount.getAmount();
	}
	
	public ListTag write()
	{
		return this.gristSet.write(new ListTag());
	}
	
	
	public ImmutableGristSet getCache()
	{
		return gristSet.asImmutable();
	}
	
	public long getRemainingCapacity()
	{
		return Math.max(0, (long) (GUTTER_CAPACITY * gutterMultiplierForSession()) - gristTotal);
	}
	
	public double gutterMultiplierForSession()
	{
		PlayerSavedData playerSavedData = PlayerSavedData.get(ServerLifecycleHooks.getCurrentServer());
		double gutterMultiplier = 0;
		for(PlayerIdentifier player : this.session.getPlayerList())
		{
			PlayerData data = playerSavedData.getData(player);
			gutterMultiplier += data.getGutterMultipler();
		}
		return gutterMultiplier;
	}
	
	/**
	 * Adds the grist from the given set into this gutter.
	 * Any grist that was added to the gutter will be removed from the given grist set,
	 * and any grist that did not fit in the gutter will therefore remain in that grist set.
	 */
	public void addGristFrom(GristSet set)
	{
		for(GristAmount amount : set.getAmounts())
		{
			GristType type = amount.getType();
			long maximumAllowed = getRemainingCapacity();
			
			if(maximumAllowed <= 0)
				return;
			
			long amountToAdd = Math.min(maximumAllowed, amount.getAmount());
			set.addGrist(type, -amountToAdd);
			this.addGristInternal(type, amountToAdd);
		}
	}
	
	/**
	 * Adds the grist to the gutter without checking the capacity. Should only be done if it is certain that the grist should fit within the capacity.
	 * To add grist to the gutter with the capacity check, see {@link #addGristFrom(GristSet)}.
	 */
	public void addGristUnchecked(GristSet set)
	{
		for(GristAmount amount : set.getAmounts())
			this.addGristInternal(amount.getType(), amount.getAmount());
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
	
	public GristSet takeFraction(double fraction)
	{
		GristSet takenGrist = new GristSet();
		double extraGrist = 0;
		
		for(GristAmount gristAmount : this.gristSet.getAmounts())
		{
			// add extraGrist to compensate for errors in the previous amounts
			double takenAmount = extraGrist + fraction*gristAmount.getAmount();
			long actualAmount = Math.round(takenAmount);
			// update extraGrist with the new error
			extraGrist = takenAmount - actualAmount;
			
			takenGrist.addGrist(gristAmount.getType(), actualAmount);
			this.addGristInternal(gristAmount.getType(), -actualAmount);
		}
		
		return takenGrist;
	}
	
	@SubscribeEvent
	public static void onServerTickEvent(TickEvent.ServerTickEvent event)
	{
		//noinspection resource
		if(event.phase == TickEvent.Phase.START && event.getServer().overworld().getGameTime() % 200 == 0)
		{
			for(Session session : SessionHandler.get(event.getServer()).getSessions())
			{
				session.getGristGutter().distributeToPlayers(session.getPlayerList(), event.getServer());
			}
		}
	}
	
	private void distributeToPlayers(Set<PlayerIdentifier> players, MinecraftServer server)
	{
		RandomSource rand = server.overworld().random;
		List<PlayerIdentifier> playerList = new ArrayList<>(players);
		Collections.shuffle(playerList, new Random(rand.nextLong()));
		
		for(PlayerIdentifier player : playerList)
		{
			tickDistributionToPlayer(player, server, rand);
		}
	}
	
	private void tickDistributionToPlayer(PlayerIdentifier player, MinecraftServer server, RandomSource rand)
	{
		PlayerData data = PlayerSavedData.getData(player, server);
		
		long spliceAmount = (long) (data.getEcheladder().getGristCapacity() * getDistributionRateModifier());
		
		NonNegativeGristSet capacity = data.getGristCache().getCapacitySet();
		GristSet gristToTransfer = this.takeWithinCapacity(spliceAmount, capacity, rand);
		GristSet remainder = data.getGristCache().addWithinCapacity(gristToTransfer, null);
		if(!remainder.isEmpty())
			throw new IllegalStateException("Took more grist than could be given to the player. Got back grist: " + remainder);
	}
	
	private double getDistributionRateModifier()
	{
		return 1D/20D;
	}
	
	private GristSet takeWithinCapacity(long amount, NonNegativeGristSet capacity, RandomSource rand)
	{
		long remaining = amount;
		GristSet takenGrist = new GristSet();
		List<GristAmount> amounts = new ArrayList<>(capacity.getAmounts());
		Collections.shuffle(amounts, new Random(rand.nextLong()));
		
		for(GristAmount capacityAmount : amounts)
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