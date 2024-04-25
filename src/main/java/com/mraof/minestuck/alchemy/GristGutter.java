package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.*;
import com.mraof.minestuck.player.*;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SessionHandler;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class that handles Grist overflow whenever you acquire too much grist.
 * @author Doro
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class GristGutter
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final int GUTTER_CAPACITY = 10000;
	
	private final MinecraftServer mcServer;
	private final Session session;
	private final NonNegativeGristSet gristSet;
	private long gristTotal;
	
	public GristGutter(MinecraftServer mcServer, Session session)
	{
		this.mcServer = mcServer;
		this.session = session;
		this.gristSet = new NonNegativeGristSet();
		this.gristTotal = 0;
	}
	
	public GristGutter(MinecraftServer mcServer, Session session, Tag tag)
	{
		this.mcServer = mcServer;
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
	
	public static Optional<GristGutter> get(ServerPlayer player)
	{
		PlayerIdentifier playerIdentifier = IdentifierHandler.encode(player);
		if(playerIdentifier == null)
			return Optional.empty();
		
		return Optional.of(get(playerIdentifier, player.server));
	}
	
	public static GristGutter get(PlayerIdentifier player, MinecraftServer mcServer)
	{
		return SessionHandler.get(mcServer).getOrCreateSession(player).getGristGutter();
	}
	
	public GristSet getCache()
	{
		return gristSet.asImmutable();
	}
	
	private Stream<PlayerIdentifier> gutterPlayers()
	{
		return this.session.getPlayers().stream().filter(player -> SburbPlayerData.get(player, mcServer).hasEntered());
	}
	
	public long getRemainingCapacity()
	{
		return Math.max(0, (long) (GUTTER_CAPACITY * gutterMultiplierForSession()) - gristTotal);
	}
	
	public double gutterMultiplierForSession()
	{
		PlayerSavedData playerSavedData = PlayerSavedData.get(mcServer);
		
		return this.gutterPlayers()
				.map(player -> playerSavedData.getOrCreateData(player).getData(MSAttachments.GUTTER_MULTIPLIER))
				.reduce(0D, Double::sum);
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
		MutableGristSet takenGrist = MutableGristSet.newDefault();
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
				session.getGristGutter().distributeToPlayers();
		}
	}
	
	private void distributeToPlayers()
	{
		RandomSource rand = mcServer.overworld().random;
		List<PlayerIdentifier> playerList = this.gutterPlayers().collect(Collectors.toCollection(ArrayList::new));
		Collections.shuffle(playerList, new Random(rand.nextLong()));
		
		for(PlayerIdentifier player : playerList)
		{
			tickDistributionToPlayer(player, rand);
		}
	}
	
	private void tickDistributionToPlayer(PlayerIdentifier player, RandomSource rand)
	{
		PlayerData data = PlayerData.get(player, mcServer);
		
		long spliceAmount = (long) (Echeladder.get(data).getGristCapacity() * getDistributionRateModifier());
		
		GristCache gristCache = GristCache.get(data);
		NonNegativeGristSet capacity = gristCache.getCapacitySet();
		GristSet gristToTransfer = this.takeWithinCapacity(spliceAmount, capacity, rand);
		GristSet remainder = gristCache.addWithinCapacity(gristToTransfer, null);
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
		MutableGristSet takenGrist = MutableGristSet.newDefault();
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