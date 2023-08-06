package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SessionHandler;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * A class that handles Grist overflow whenever you acquire too much grist.
 * @author Doro
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class GristGutter
{
	private static final Logger LOGGER = LogManager.getLogger();
	
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
	
	public GristGutter(Session session, Tag tag)
	{
		this.session = session;
		this.gristSet = NonNegativeGristSet.CODEC.parse(NbtOps.INSTANCE, tag).resultOrPartial(LOGGER::error).orElse(new NonNegativeGristSet());
		this.gristTotal = 0;
		for(GristAmount amount : this.gristSet.asAmounts())
			this.gristTotal += amount.amount();
	}
	
	public Tag write()
	{
		return NonNegativeGristSet.CODEC.encodeStart(NbtOps.INSTANCE, this.gristSet).resultOrPartial(LOGGER::error).orElse(EndTag.INSTANCE);
	}
	
	public GristSet getCache()
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
	public void addGristFrom(MutableGristSet set)
	{
		for(GristAmount amount : set.asAmounts())
		{
			GristType type = amount.type();
			long maximumAllowed = getRemainingCapacity();
			
			if(maximumAllowed <= 0)
				return;
			
			long amountToAdd = Math.min(maximumAllowed, amount.amount());
			set.add(type, -amountToAdd);
			this.addGristInternal(type, amountToAdd);
		}
	}
	
	/**
	 * Adds the grist to the gutter without checking the capacity. Should only be done if it is certain that the grist should fit within the capacity.
	 * To add grist to the gutter with the capacity check, see {@link #addGristFrom(MutableGristSet)}.
	 */
	public void addGristUnchecked(GristSet set)
	{
		for(GristAmount amount : set.asAmounts())
			this.addGristInternal(amount.type(), amount.amount());
	}
	
	/**
	 * The grist set is currently only modified here,
	 * which lets us be certain that gutterTotal is accurate.
	 */
	private void addGristInternal(GristType type, long amount)
	{
		this.gristSet.add(type, amount);
		this.gristTotal += amount;
	}
	
	public MutableGristSet takeFraction(double fraction)
	{
		MutableGristSet takenGrist = new MutableGristSet();
		double extraGrist = 0;
		
		for(GristAmount gristAmount : this.gristSet.asAmounts())
		{
			// add extraGrist to compensate for errors in the previous amounts
			double takenAmount = extraGrist + fraction*gristAmount.amount();
			long actualAmount = Math.round(takenAmount);
			// update extraGrist with the new error
			extraGrist = takenAmount - actualAmount;
			
			takenGrist.add(gristAmount.type(), actualAmount);
			this.addGristInternal(gristAmount.type(), -actualAmount);
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
	
	private MutableGristSet takeWithinCapacity(long amount, NonNegativeGristSet capacity, RandomSource rand)
	{
		long remaining = amount;
		MutableGristSet takenGrist = new MutableGristSet();
		List<GristAmount> amounts = new ArrayList<>(capacity.asAmounts());
		Collections.shuffle(amounts, new Random(rand.nextLong()));
		
		for(GristAmount capacityAmount : amounts)
		{
			GristType type = capacityAmount.type();
			long amountInGutter = this.gristSet.getGrist(type);
			if(amountInGutter > 0)
			{
				long takenAmount = Math.min(remaining, Math.min(capacityAmount.amount(), amountInGutter));
				this.addGristInternal(type, -takenAmount);
				takenGrist.add(type, takenAmount);
				remaining -= takenAmount;
				if(remaining <= 0)
					break;
			}
		}
		return takenGrist;
	}
}