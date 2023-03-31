package com.mraof.minestuck.world.lands;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import net.minecraftforge.registries.IForgeRegistry;
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
		TerrainLandType aspect = selectRandomAspect(usedAspects, createByGroupMap(LandTypes.TERRAIN_REGISTRY.get()), aspect2::isAspectCompatible);
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
		if(aspectTerrain != null)
		{
			landAspect = selectRandomAspect(usedAspects, createByGroupMap(LandTypes.TITLE_REGISTRY.get()), aspect -> aspect.getAspect() == titleAspect && aspect.isAspectCompatible(aspectTerrain));
		} else
			landAspect = selectRandomAspect(usedAspects, createByGroupMap(LandTypes.TITLE_REGISTRY.get()), aspect -> aspect.getAspect() == titleAspect);
		
		if(landAspect != null)
			return landAspect;
		else return LandTypes.TITLE_NULL.get();
	}
	
	private <A extends ILandType> Map<ResourceLocation, List<A>> createByGroupMap(IForgeRegistry<A> registry)
	{
		Map<ResourceLocation, List<A>> groupMap = Maps.newHashMap();
		for(A landType : registry)
		{
			if(landType.canBePickedAtRandom())
				groupMap.computeIfAbsent(landType.getGroup(), _landType -> Lists.newArrayList()).add(landType);
		}
		return groupMap;
	}
	
	private <A extends ILandType> A selectRandomAspect(List<A> usedAspects, Map<ResourceLocation, List<A>> groupMap, Predicate<A> condition)
	{
		List<List<A>> list = Lists.newArrayList();
		for(List<A> aspects : groupMap.values())
		{
			List<A> variantList = Lists.newArrayList(aspects);
			variantList.removeIf(condition.negate());
			if(!variantList.isEmpty())
				list.add(variantList);
		}
		
		List<A> groupList = pickOneFromUsage(list, usedAspects, (variants, used) -> variants.get(0).getGroup().equals(used.getGroup()));
		if(groupList == null)
			return null;
		return pickOneFromUsage(groupList, usedAspects, Object::equals);
	}
	
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
