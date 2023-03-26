package com.mraof.minestuck.world.lands;

import com.google.common.collect.Lists;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.world.DynamicDimensions;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public final class LandTypeGenerator
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Random random;
	
	
	public LandTypeGenerator(long seed)
	{
		random = new Random(seed);
	}
	
	/**
	 * Generates a random land aspect, weighted based on a player's title.
	 */
	public TerrainLandType getTerrainAspect(TitleLandType aspect2, List<TerrainLandType> usedAspects)
	{
		TerrainLandType aspect = selectRandomAspect(usedAspects, LandTypeSelection.terrainAlternatives(), aspect2::isAspectCompatible);
		if(aspect != null)
			return aspect;
		else
		{
			LOGGER.error("No land aspect is compatible with the title aspect {}! Defaulting to null land aspect.", LandTypes.TITLE_REGISTRY.get().getKey(aspect2));
			return LandTypes.TERRAIN_NULL.get();
		}
	}
	
	public TitleLandType getTitleAspect(TerrainLandType aspectTerrain, EnumAspect titleAspect, List<TitleLandType> usedAspects)
	{
		TitleLandType landAspect;
		var alternatives = LandTypeSelection.titleAlternatives(titleAspect);
		if(aspectTerrain != null)
		{
			landAspect = selectRandomAspect(usedAspects, alternatives, aspect -> aspect.isAspectCompatible(aspectTerrain));
		} else
			landAspect = selectRandomAspect(usedAspects, alternatives, aspect -> true);
		
		if(landAspect != null)
			return landAspect;
		else return LandTypes.TITLE_NULL.get();
	}
	
	private <A extends ILandType> A selectRandomAspect(List<A> usedAspects, Iterable<? extends Collection<A>> groups, Predicate<A> condition)
	{
		List<ChoiceEntry<A>> list = Lists.newArrayList();
		for(Collection<A> group : groups)
		{
			List<A> variantList = Lists.newArrayList(group);
			variantList.removeIf(condition.negate());
			if(!variantList.isEmpty())
				list.add(new ChoiceEntry<>(variantList, group));
		}
		
		ChoiceEntry<A> entry = pickOneFromUsage(list, usedAspects, (variants, used) -> variants.all().contains(used));
		if(entry == null)
			return null;
		return pickOneFromUsage(entry.allowed, usedAspects, Object::equals);
	}
	
	private record ChoiceEntry<A>(List<A> allowed, Collection<A> all){}
	
	private <A extends ILandType, B> B pickOneFromUsage(List<B> list, List<A> usedAspects, BiPredicate<B, A> matchPredicate)
	{
		if(list.isEmpty())
			return null;
		else if(list.size() == 1)
			return list.get(0);
		else
		{
			int[] useCount = new int[list.size()];
			for(A usedAspect : usedAspects)
			{
				for(int i = 0; i < list.size(); i++)
					if(matchPredicate.test(list.get(i), usedAspect))
						useCount[i]++;
			}
			
			ArrayList<B> unusedEntries = new ArrayList<>();
			for(int i = 0; i < list.size(); i++)	//Check for unused aspects
				if(useCount[i] == 0)
					unusedEntries.add(list.get(i));
			
			if(unusedEntries.size() > 0)
				return unusedEntries.get(random.nextInt(unusedEntries.size()));
			
			double randCap = 0;
			for(int value : useCount) randCap += 1D / value;
			
			double rand = random.nextDouble()*randCap;
			
			for(int i = 0; i < useCount.length; i++)
				if(rand < 1D/useCount[i])
				{
					return list.get(i);
				}
				else rand -= 1D/useCount[i];
			
			throw new IllegalStateException("This should not happen!");
		}
	}
	
	/**
	 * Registers a new dimension for a land. Returns the type of the new land.
	 * @param player The player whose Land is being created
	 * @param aspects Land aspects that the land should have
	 * @return Returns the dimension of the newly created land.
	 */
	public static ResourceKey<Level> createLandDimension(MinecraftServer server, PlayerIdentifier player, LandTypePair aspects)
	{
		String base = "minestuck:land_"+player.getUsername().toLowerCase();
		ResourceLocation dimensionName;
		try
		{
			dimensionName = new ResourceLocation(base);
		} catch(ResourceLocationException e)
		{
			base = "minestuck:land";
			dimensionName = new ResourceLocation(base);
		}
		
		return DynamicDimensions.createLand(server, dimensionName, aspects);
	}
}
