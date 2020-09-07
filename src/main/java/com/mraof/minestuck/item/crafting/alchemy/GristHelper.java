package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.event.GristDropsEvent;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;

import static com.mraof.minestuck.MinestuckConfig.showGristChanges;

public class GristHelper
{
	private static Random random = new Random();
	
	/**
	 * Returns a random grist type. Used for creating randomly aligned underlings.
	 */
	public static GristType getPrimaryGrist()
	{
		float totalWeight = 0;
		List<GristType> typeList = new ArrayList<>();
		for(GristType type : GristTypes.values())
		{
			if(type.getRarity() > 0 && type != GristTypes.ARTIFACT.get())
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
	public static GristType getSecondaryGrist(GristType primary)
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
		GristType primary = entity.getGristType();
		GristType secondary = getSecondaryGrist(primary);
		
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
	public static long getGrist(World world, PlayerIdentifier player, GristType type)
	{
		return PlayerSavedData.getData(player, world).getGristCache().getGrist(type);
	}
	
	public static long getGrist(World world, PlayerIdentifier player, Supplier<GristType> type)
	{
		return getGrist(world, player, type.get());
	}
	
	public static boolean canAfford(ServerPlayerEntity player, GristSet cost)
	{
		return canAfford(PlayerSavedData.getData(player).getGristCache(), cost);
	}
	
	public static boolean canAfford(World world, PlayerIdentifier player, GristSet cost)
	{
		return canAfford(PlayerSavedData.getData(player, world).getGristCache(), cost);
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
	public static void decrease(World world, PlayerIdentifier player, GristSet set)
	{
		increase(world, player, set.copy().scale(-1));
	}
	
	public static void increase(World world, PlayerIdentifier player, GristSet set)
	{
		Objects.requireNonNull(world);
		Objects.requireNonNull(player);
		Objects.requireNonNull(set);
		PlayerData data = PlayerSavedData.getData(player, world);
		NonNegativeGristSet newCache = new NonNegativeGristSet(data.getGristCache());
		newCache.addGrist(set);
		data.setGristCache(newCache);
	}
	
	public static void notify(MinecraftServer server, PlayerIdentifier player, GristSet set)
	{
		if(showGristChanges.get())
		{
			Map<GristType, Long> reqs = set.getMap();
			for(Entry<GristType, Long> pairs : reqs.entrySet())
			{
				ITextComponent type = pairs.getKey().getDisplayName();
				long difference = pairs.getValue();
				sendGristMessage(server, player, new TranslationTextComponent("You gained %s %s grist.", difference, type));
			}
		}
	}
	
	public static void notifyEditPlayer(MinecraftServer server, PlayerIdentifier player, GristSet set, boolean increase)
	{
		if(showGristChanges.get())
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
				ITextComponent type = pairs.getKey().getDisplayName();
				long difference = pairs.getValue();
				if(increase)
				{
					sendGristMessage(server, IdentifierHandler.encode(ed.getEditor()), new TranslationTextComponent("You have refunded %s of %s's %s grist.", difference, player.getUsername(), type));
				} else
				{
					sendGristMessage(server, IdentifierHandler.encode(ed.getEditor()), new TranslationTextComponent("You have spent %s of %s's %s grist.", difference, player.getUsername(), type));
				}
			}
		}
	}
	
	private static void sendGristMessage(MinecraftServer server, PlayerIdentifier player, ITextComponent message)
	{
		if(player != null)
		{
			ServerPlayerEntity client = player.getPlayer(server);
			if(client != null)
			{
				client.sendStatusMessage(message, true);
			}
		}
	}
	
}