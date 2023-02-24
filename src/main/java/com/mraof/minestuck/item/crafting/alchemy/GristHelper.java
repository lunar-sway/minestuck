package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.event.GristDropsEvent;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SessionHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;

import static com.mraof.minestuck.world.storage.ClientPlayerData.rung;

public class GristHelper
{
	/**
	 * Returns a random grist type. Used for creating randomly aligned underlings.
	 */
	public static GristType getPrimaryGrist(Random random)
	{
		float totalWeight = 0;
		List<GristType> typeList = new ArrayList<>();
		for(GristType type : GristTypes.values())
		{
			if(type.isUnderlingType() && type.isInCategory(GristType.SpawnCategory.ANY))
			{
				typeList.add(type);
				totalWeight += type.getRarity();
			}
		}
		
		float weight = random.nextFloat() * totalWeight;
		for(GristType type : typeList)
		{
			weight -= type.getRarity();
			if(weight < 0)
				return type;
		}
		throw new IllegalStateException("Should never get here.");
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
		double effectivePowerLevel = 1;
		for(PlayerIdentifier id : damageMap.keySet())
		{
			MinecraftServer server = entity.getServer();
			Level level = entity.getLevel();
			Session session = SessionHandler.get(level).getPlayerSession(id);
			int sesPL = (int) session.getSessionPowerlevel(server);
			effectivePowerLevel += sesPL;
		}
		multiplier = (multiplier * effectivePowerLevel / damageMap.keySet().size());
		
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
		
		
		return set.capGrist(gristCap);//returns the result of capGrist
	}
	
	public static final int[] rungGrist =//never set this below 20.
			{20,30,40,50,60,70,80,90,100,110,120,130,140,150,160,170,
					180,190,200,240,250,260,265,270,275,280,285,290,295,300,
					350,400,450,455,500,1000,2000,3000,4000,5000,6000,7000,
					8000,9000,10000,20000,30000,40000,50000,500000};
	// the function that controls how much grist is spliced from the gutter
	// will crash the game if set below 20
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