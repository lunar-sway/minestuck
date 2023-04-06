package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.event.GristDropsEvent;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
				.map(type -> WeightedEntry.wrap(type, Math.round(type.getRarity() * 100))).toList();
		
		return WeightedRandom.getRandomItem(random, typeList).orElseThrow().getData();
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
		
		GristSet set = new GristSet();
		set.addGrist(GristTypes.BUILD, (int) (2 * multiplier + random.nextDouble() * 18 * multiplier));
		set.addGrist(primary, (int) (1 * multiplier + random.nextDouble() * 9 * multiplier));
		set.addGrist(secondary, (int) (0.5 * multiplier + random.nextDouble() * 4 * multiplier));
		
		GristDropsEvent event = new GristDropsEvent(entity, damageMap, set, primary, secondary, multiplier);
		if(MinecraftForge.EVENT_BUS.post(event))
			return null;
		
		return event.getNewDrops();
		
	}
	
	//TODO figure out how best to check cache capacity client-side
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
	
}