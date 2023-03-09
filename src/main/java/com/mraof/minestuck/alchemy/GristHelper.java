package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.event.GristDropsEvent;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.GristToastPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SessionHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.mraof.minestuck.player.ClientPlayerData.rung;

public class GristHelper
{
	
	/**
	 * An enum for indicating where the grist notifications comes from.
	 */
	public enum EnumSource {
		CLIENT, //The SBURB client.
		SERVER, //The SBURB server.
		SENDGRIST, //The /sendGrist command. (Might be replaced when grist torrent is implemented.)
		CONSOLE //For things like the /grist command.
	}
	
	/**
	 * Returns a random grist type. Used for creating randomly aligned underlings.
	 */
	public static GristType getPrimaryGrist(RandomSource random)
	{
		List<WeightedEntry.Wrapper<GristType>> typeList = GristType.SpawnCategory.ANY.gristTypes()
				.map(type -> WeightedEntry.wrap(type, Math.round(type.getRarity() * 100))).collect(Collectors.toList());
		
		return WeightedRandom.getRandomItem(random, typeList).orElseThrow(null).getData();
	}
	
	/**
	 * Returns a secondary grist type based on primary grist
	 */
	public static GristType getSecondaryGrist(RandomSource random, GristType primary)
	{
		List<GristType> secondaryTypes = primary.getSecondaryTypes();
		if(secondaryTypes.size() > 0)
			return secondaryTypes.get(random.nextInt(secondaryTypes.size()));
		else return primary;
	}
	
	
	/**
	 * Returns a GristSet representing the drops from an underling, given the underling's type and a static loot multiplier.
	 */
	public static GristSet generateUnderlingGristDrops(UnderlingEntity entity, Map<PlayerIdentifier, Double> damageMap, double multiplier)
	{
		RandomSource random = entity.getRandom();
		GristType primary = entity.getGristType();
		GristType secondary = getSecondaryGrist(random, primary);
		double effectivePowerLevel = 1;
		
		GristSet set = new GristSet();
		set.addGrist(GristTypes.BUILD, (int) (2 * multiplier + random.nextDouble() * 18 * multiplier));
		set.addGrist(primary, (int) (1 * multiplier + random.nextDouble() * 9 * multiplier));
		set.addGrist(secondary, (int) (0.5 * multiplier + random.nextDouble() * 4 * multiplier));
		
		GristDropsEvent event = new GristDropsEvent(entity, damageMap, set, primary, secondary, multiplier);
		if(MinecraftForge.EVENT_BUS.post(event))
			return null;
		
		return event.getNewDrops();
		
	}
	
	/**
	 * A shortened statement to obtain a certain grist count.
	 */
	public static long getGrist(Level level, PlayerIdentifier player, GristType type)
	{
		return PlayerSavedData.getData(player, level).getGristCache().getGrist(type);
	}
	
	public static long getGrist(Level level, PlayerIdentifier player, Supplier<GristType> type)
	{
		return getGrist(level, player, type.get());
	}
	
	public static boolean canAfford(ServerPlayer player, GristSet cost)
	{
		return canAfford(PlayerSavedData.getData(player).getGristCache(), cost);
	}
	
	public static boolean canAfford(Level level, PlayerIdentifier player, GristSet cost)
	{
		return canAfford(PlayerSavedData.getData(player, level).getGristCache(), cost);
	}
	
	public static boolean canAfford(GristSet base, GristSet cost)
	{
		if(base == null || cost == null)
		{
			return false;
		}
		Map<GristType, Long> reqs = cost.getMap();
		if(reqs != null)
		{
			for(Entry<GristType, Long> pairs : reqs.entrySet())
			{
				GristType type = pairs.getKey();
				long need = pairs.getValue();
				long have = base.getGrist(type);
				
				if(need > have) return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Uses the encoded version of the username!
	 */
	public static void decrease(Level level, PlayerIdentifier player, GristSet set)
	{
		increase(level, player, set.copy().scale(-1));
	}
	
	public static void decreaseAndNotify(Level level, PlayerIdentifier player, GristSet set, GristHelper.EnumSource source)
	{
		decrease(level, player, set);
		GristSet total = PlayerSavedData.getData(player, level).getGristCache();
		notify(level.getServer(), player, set, total, source, false);
	}
	
	public static void increase(Level level, PlayerIdentifier player, GristSet set)
	{
		Objects.requireNonNull(level);
		Session session = SessionHandler.get(level).getPlayerSession(player);
		GristGutter gutter = session.getGristGutter();
		
		Objects.requireNonNull(player);
		Objects.requireNonNull(set);
		PlayerData data = PlayerSavedData.getData(player, level);
		NonNegativeGristSet newCache = new NonNegativeGristSet(data.getGristCache());
		newCache.addGrist(set);
		int gristCap = rungGrist[rung];
		
		GristSet overflowedGrist = limitGristByPlayerRung(level, player, newCache);
		gutter.addGrist(overflowedGrist);//sends grist overflow to gutter
		data.setGristCache(newCache);
		ServerPlayer playerEntity = player.getPlayer(level.getServer());
		if(playerEntity != null)
		{
			gutter.spillGrist(level, playerEntity);//this isn't currently being used
		}
	}
	public static GristSet limitGristByPlayerRung(Level level, PlayerIdentifier player, GristSet set)
	{
		int rung = PlayerSavedData.getData(player, level).getEcheladder().getRung();
		int gristCap = rungGrist[rung];//uses the values in the rungGrist array to determine the current grist cap
		if (gristCap < 0)
		{
			return null;
		}
		else
		{
			return set.capGrist(gristCap);//returns the result of capGrist
		}
	}
	public static final int[] rungGrist =// will crash the game if set below 20
			{20,30,40,50,60,70,80,90,100,110,120,130,140,150,160,170,
					180,190,200,240,250,260,265,270,275,280,285,290,295,300,
					350,400,450,455,500,1000,2000,3000,4000,5000,6000,7000,
					8000,9000,10000,20000,30000,40000,50000,90000000};// the function that controls how much grist is spliced from the gutter
	
	public static void increaseAndNotify(Level level, PlayerIdentifier player, GristSet set, GristHelper.EnumSource source)
	{
		increase(level, player, set);
		GristSet total = PlayerSavedData.getData(player, level).getGristCache();
		notify(level.getServer(), player, set, total, source, true);
	}
	
	/**
	 * Sends a request to make a client-side Toast Notification for incoming/outgoing grist, if enabled in the config.
	 * @param server Used for getting the ServerPlayer from their PlayerIdentifier
	 * @param player The Player that the notification should appear for.
	 * @param set The grist type and value pairs associated with the notifications. There can be multiple pairs in the set, but usually only one.
	 * @param source Indicates where the notification is coming from. See EnumSource.
	 * @param increase Indicates whether the grist is gained or lost.
	 */
	public static void notify(MinecraftServer server, PlayerIdentifier player, GristSet set, GristSet total, GristHelper.EnumSource source, boolean increase)
	{
		if(MinestuckConfig.SERVER.showGristChanges.get())
		{
			int cacheLimit = rungGrist[rung];
			GristToastPacket gristToastPacket = new GristToastPacket(set, source, increase, cacheLimit, total);
			
			if(player.getPlayer(server) != null)
				MSPacketHandler.sendToPlayer(gristToastPacket, player.getPlayer(server));
			
			if (source == EnumSource.SERVER)
			{
				SburbConnection sc = SkaianetHandler.get(server).getActiveConnection(player);
				if(sc == null)
					return;
				
				EditData ed = ServerEditHandler.getData(server, sc);
				if(ed == null)
					return;
				
				if(!player.appliesTo(ed.getEditor()))
					MSPacketHandler.sendToPlayer(gristToastPacket, ed.getEditor());

			}
		}
	}
}