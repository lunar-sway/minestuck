package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypeSpawnCategory;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.event.GristDropsEvent;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.core.Holder;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class GristHelper
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static Tag encodeGristType(GristType gristType)
	{
		return GristTypes.REGISTRY.byNameCodec().encodeStart(NbtOps.INSTANCE, gristType).getOrThrow(false, LOGGER::error);
	}
	
	public static Optional<GristType> parseGristType(Tag tag)
	{
		return GristTypes.REGISTRY.byNameCodec().parse(NbtOps.INSTANCE, tag).resultOrPartial(LOGGER::error);
	}
	
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
		return primary.getSecondaryTypes().flatMap(set -> set.getRandomElement(random).map(Holder::value))
				.orElse(primary);
	}
	
	
	/**
	 * Returns a GristSet representing the drops from an underling, given the underling's type and a static loot multiplier.
	 */
	public static MutableGristSet generateUnderlingGristDrops(UnderlingEntity entity, Map<PlayerIdentifier, Double> damageMap, double multiplier)
	{
		RandomSource random = entity.getRandom();
		GristType primary = entity.getGristType();
		GristType secondary = getSecondaryGrist(random, primary);
		
		MutableGristSet set = MutableGristSet.newDefault();
		set.add(GristTypes.BUILD, (int) (2 * multiplier + random.nextDouble() * 18 * multiplier));
		set.add(primary, (int) (1 * multiplier + random.nextDouble() * 9 * multiplier));
		set.add(secondary, (int) (0.5 * multiplier + random.nextDouble() * 4 * multiplier));
		
		GristDropsEvent event = NeoForge.EVENT_BUS.post(new GristDropsEvent(entity, damageMap, set, primary, secondary, multiplier));
		if(event.isCanceled())
			return null;
		
		return event.getNewDrops();
		
	}
}
