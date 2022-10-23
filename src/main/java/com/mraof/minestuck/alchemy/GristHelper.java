package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.event.GristDropsEvent;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;

public class GristHelper
{
	/**
	 * Returns a random grist type. Used for creating randomly aligned underlings.
	 */
	public static GristType getPrimaryGrist(Random random)
	{
		List<WeightedEntry.Wrapper<GristType>> typeList = GristType.SpawnCategory.ANY.gristTypes()
				.map(type -> WeightedEntry.wrap(type, Math.round(type.getRarity() * 100))).toList();
		
		return WeightedRandom.getRandomItem(random, typeList).orElseThrow().getData();
	}
	
	/**
	 * Returns a secondary grist type based on primary grist
	 */
	public static GristType getSecondaryGrist(Random random, GristType primary)
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
		Random random = entity.getRandom();
		GristType primary = entity.getGristType();
		GristType secondary = getSecondaryGrist(random, primary);
		
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
	
	public static void increase(Level level, PlayerIdentifier player, GristSet set)
	{
		Objects.requireNonNull(level);
		Objects.requireNonNull(player);
		Objects.requireNonNull(set);
		PlayerData data = PlayerSavedData.getData(player, level);
		NonNegativeGristSet newCache = new NonNegativeGristSet(data.getGristCache());
		newCache.addGrist(set);
		data.setGristCache(newCache);
	}
	
	public static void notify(MinecraftServer server, PlayerIdentifier player, GristSet set)
	{
		if(MinestuckConfig.SERVER.showGristChanges.get())
		{
			Map<GristType, Long> reqs = set.getMap();
			for(Entry<GristType, Long> pairs : reqs.entrySet())
			{
				Component type = pairs.getKey().getDisplayName();
				long difference = pairs.getValue();
				sendGristMessage(server, player, new TranslatableComponent("You gained %s %s grist.", difference, type));
			}
		}
	}
	
	public static void notifyEditPlayer(MinecraftServer server, PlayerIdentifier player, GristSet set, boolean increase)
	{
		if(MinestuckConfig.SERVER.showGristChanges.get())
		{
			SburbConnection sc = SkaianetHandler.get(server).getActiveConnection(player);
			if(sc == null)
				return;
			
			EditData ed = ServerEditHandler.getData(server, sc);
			if(ed == null)
				return;
			
			Map<GristType, Long> reqs = set.getMap();
			for(Entry<GristType, Long> pairs : reqs.entrySet())
			{
				Component type = pairs.getKey().getDisplayName();
				long difference = pairs.getValue();
				if(increase)
				{
					sendGristMessage(server, IdentifierHandler.encode(ed.getEditor()), new TranslatableComponent("You have refunded %s of %s's %s grist.", difference, player.getUsername(), type));
				} else
				{
					sendGristMessage(server, IdentifierHandler.encode(ed.getEditor()), new TranslatableComponent("You have spent %s of %s's %s grist.", difference, player.getUsername(), type));
				}
			}
		}
	}
	
	private static void sendGristMessage(MinecraftServer server, PlayerIdentifier player, Component message)
	{
		if(player != null)
		{
			ServerPlayer client = player.getPlayer(server);
			if(client != null)
			{
				client.displayClientMessage(message, true);
			}
		}
	}
	
}