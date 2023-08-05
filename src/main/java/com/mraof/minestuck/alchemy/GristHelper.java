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

public class GristHelper
{
	
	/**
	 * An enum for indicating where the grist notifications comes from.
	 */
	public enum EnumSource {
		CLIENT, //The SBURB client.
		SERVER, //The SBURB server.
		SENDGRIST, //The /sendGrist command. (Might be replaced when grist torrent is implemented.)
		GUTTER, //Is headed to the displayed players grist gutter
		CONSOLE //For things like the /grist command.
	}
	
	/**
	 * Returns a random grist type. Used for creating randomly aligned underlings.
	 */
	public static GristType getPrimaryGrist(RandomSource random)
	{
		List<WeightedEntry.Wrapper<GristType>> typeList = GristTypeSpawnCategory.ANY.gristTypes()
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
	public static MutableGristSet generateUnderlingGristDrops(UnderlingEntity entity, Map<PlayerIdentifier, Double> damageMap, double multiplier)
	{
		RandomSource random = entity.getRandom();
		GristType primary = entity.getGristType();
		GristType secondary = getSecondaryGrist(random, primary);
		
		MutableGristSet set = new MutableGristSet();
		set.add(GristTypes.BUILD, (int) (2 * multiplier + random.nextDouble() * 18 * multiplier));
		set.add(primary, (int) (1 * multiplier + random.nextDouble() * 9 * multiplier));
		set.add(secondary, (int) (0.5 * multiplier + random.nextDouble() * 4 * multiplier));
		
		GristDropsEvent event = new GristDropsEvent(entity, damageMap, set, primary, secondary, multiplier);
		if(MinecraftForge.EVENT_BUS.post(event))
			return null;
		
		return event.getNewDrops();
		
	}
}