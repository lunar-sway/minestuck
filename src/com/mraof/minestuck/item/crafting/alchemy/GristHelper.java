package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.EditData;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.event.GristDropsEvent;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;

public class GristHelper
{
	private static Random random = new Random();
	
	/**
	 * Returns a random grist type. Used for creating randomly aligned underlings.
	 */
	public static GristType getPrimaryGrist()
	{
		while (true)
		{
			GristType randGrist = GristTypes.MARBLE;//GristType.values().get(random.nextInt(GristType.values().size()));
			if (randGrist.getRarity() > random.nextFloat() && randGrist != GristTypes.ARTIFACT)
				return randGrist;
		}
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
		set.addGrist(GristTypes.BUILD, (int)(2*multiplier + random.nextDouble()*18*multiplier));
		set.addGrist(primary, (int)(1*multiplier + random.nextDouble()*9*multiplier));
		set.addGrist(secondary, (int)(0.5*multiplier + random.nextDouble()*4*multiplier));
		
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
	
	public static boolean canAfford(ServerPlayerEntity player, GristSet cost)
	{
		return canAfford(PlayerSavedData.getData(player).getGristCache(), cost);
	}
	
	public static boolean canAfford(World world, PlayerIdentifier player, GristSet cost)
	{
		return canAfford(PlayerSavedData.getData(player, world).getGristCache(), cost);
	}
	
	public static boolean canAfford(GristSet base, GristSet cost) {
		if (base == null || cost == null) {return false;}
		Map<GristType, Long> reqs = cost.getMap();
		
		if (reqs != null) {
			for (Entry<GristType, Long> pairs : reqs.entrySet())
			{
				GristType type = pairs.getKey();
				long need = pairs.getValue();
				long have = base.getGrist(type);

				if (need > have) return false;
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
	
	private static void notify(MinecraftServer server, PlayerIdentifier player, ITextComponent type, long difference, String action)
	{
		if(MinestuckConfig.showGristChanges.get())
		{
			if (player != null)
			{
				ServerPlayerEntity client = player.getPlayer(server);
				if(client != null)
				{
					//"true" sends the message to the action bar (like bed messages), while "false" sends it to the chat.
					client.sendStatusMessage(new TranslationTextComponent("You " + action + " " + difference + " " + type + " grist."), true);//TODO Translation
				}
			}
		}
	}
	
	private static void notifyEditPlayer(MinecraftServer server, PlayerIdentifier player, ITextComponent type, long difference, String action)
	{
		SburbConnection sc = SkaianetHandler.get(server).getActiveConnection(player);
		if(sc == null)
			return;
		
		EditData ed = ServerEditHandler.getData(server, sc);
		if(ed == null)
			return;
		
		notify(server, IdentifierHandler.encode(ed.getEditor()), type, difference, action);
	}
}