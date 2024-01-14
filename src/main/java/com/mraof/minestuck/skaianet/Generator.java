package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.player.*;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.gen.LandTypeGenerator;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A more specific name is probably good, if anyone has any suggestion.
 * Handles generation of things like land types and titles, which usually depend on what has already been generated in the session.
 * Only meant to be used skaianet-internally during entry preparations and during predefine data.
 * @author Kirderf1
 */
public class Generator
{
	static Title generateTitle(Session session, Set<EnumAspect> availableAspects, PlayerIdentifier ignore) throws SkaianetException
	{
		Random random = new Random();	//TODO seed based on player and world in a good way
		
		Set<Title> usedTitles = getUsedTitles(session, ignore);
		
		List<EnumAspect> unusedAspects = unusedAspects(availableAspects, usedTitles);
		List<EnumClass> unusedClasses = unusedClasses(usedTitles);
		
		if(!unusedAspects.isEmpty() && !unusedClasses.isEmpty())
		{
			EnumAspect a = getRandomFromList(unusedAspects, random);
			EnumClass c = getRandomFromList(unusedClasses, random);
			
			return new Title(c, a);
		} else if(!unusedAspects.isEmpty())
		{
			EnumAspect a = getRandomFromList(unusedAspects, random);
			EnumClass c = getRandomFromList(EnumClass.valuesStream().toList(), random);
			
			return new Title(c, a);
		} else if(!unusedClasses.isEmpty())
		{
			EnumAspect a = getRandomFromList(List.copyOf(availableAspects), random);
			EnumClass c = getRandomFromList(unusedClasses, random);
			
			return new Title(c, a);
		} else
		{
			List<Title> unusedTitles = availableAspects.stream().flatMap(a -> EnumClass.valuesStream().map(c -> new Title(c, a)))
					.filter(title -> !usedTitles.contains(title)).toList();
			
			if(!unusedTitles.isEmpty())
				return getRandomFromList(unusedTitles, random);
			else
			{
				EnumAspect a = getRandomFromList(List.copyOf(availableAspects), random);
				EnumClass c = getRandomFromList(EnumClass.valuesStream().toList(), random);
				
				return new Title(c, a);
			}
		}
	}
	
	static Set<Title> getUsedTitles(Session session, @Nullable PlayerIdentifier ignore)
	{
		Set<Title> titles = new HashSet<>();
		for(PlayerIdentifier player : session.getPlayers())
		{
			if(player.equals(ignore))
				continue;
			
			Title title = PlayerSavedData.getData(player, session.skaianetHandler.mcServer).getTitle();
			if(title != null)
				titles.add(title);
			else
				session.skaianetHandler.predefineData(player).ifPresent(data -> {
					if(data.getTitle() != null)
						titles.add(data.getTitle());
				});
		}
		
		return titles;
	}
	
	private static List<EnumAspect> unusedAspects(Set<EnumAspect> base, Set<Title> usedTitles)
	{
		Set<EnumAspect> usedAspects = usedTitles.stream().map(Title::getHeroAspect).collect(Collectors.toSet());
		return base.stream().filter(a -> !usedAspects.contains(a)).collect(Collectors.toList());
	}
	
	private static List<EnumClass> unusedClasses(Set<Title> usedTitles)
	{
		Set<EnumClass> usedClasses = usedTitles.stream().map(Title::getHeroClass).collect(Collectors.toSet());
		return EnumClass.valuesStream().filter(c -> !usedClasses.contains(c)).collect(Collectors.toList());
	}
	
	static TitleLandType generateWeightedTitleLandType(Session session, EnumAspect aspect, @Nullable TerrainLandType terrainType, PlayerIdentifier ignore)
	{
		Random random = new Random();
		LandTypeGenerator landGen = new LandTypeGenerator(random.nextLong());	//TODO seed based on player and world in a good way
		
		List<TitleLandType> usedTypes = getUsedTitleLandTypes(session, ignore);
		
		boolean hasFrogs = usedTypes.contains(LandTypes.FROGS.get());
		
		//if(aspect == EnumAspect.SPACE && !hasFrogs)
		//Maybe force it if we so want?
		
		return landGen.getTitleAspect(terrainType, aspect, usedTypes);
	}
	
	static List<TitleLandType> getUsedTitleLandTypes(Session session)
	{
		return getUsedTitleLandTypes(session, null);
	}
	
	static List<TitleLandType> getUsedTitleLandTypes(Session session, @Nullable PlayerIdentifier ignore)
	{
		List<TitleLandType> types = new ArrayList<>();
		for(PlayerIdentifier player : session.getPlayers())
		{
			if(player.equals(ignore))
				continue;
			
			LandTypePair.getTypes(session.skaianetHandler.mcServer, session.skaianetHandler.getOrCreateData(player).getLandDimension())
					.ifPresent(landTypes -> types.add(landTypes.getTitle()));
			
			session.skaianetHandler.predefineData(player).ifPresent(data -> {
				if(data.getTitleLandType() != null)
					types.add(data.getTitleLandType());
			});
		}
		
		return types;
	}
	static TerrainLandType generateWeightedTerrainLandType(Session session, TitleLandType titleType, PlayerIdentifier ignore)
	{
		Random random = new Random();
		LandTypeGenerator landGen = new LandTypeGenerator(random.nextLong());	//TODO seed based on player and world in a good way
		
		List<TerrainLandType> usedTypes = getUsedTerrainLandTypes(session, ignore);
		
		return landGen.getTerrainAspect(titleType, usedTypes);
	}
	
	static List<TerrainLandType> getUsedTerrainLandTypes(Session session, @Nullable PlayerIdentifier ignore)
	{
		List<TerrainLandType> types = new ArrayList<>();
		for(PlayerIdentifier player : session.getPlayers())
		{
			if(player.equals(ignore))
				continue;
			
			LandTypePair.getTypes(session.skaianetHandler.mcServer, session.skaianetHandler.getOrCreateData(player).getLandDimension())
					.ifPresent(landTypes -> types.add(landTypes.getTerrain()));
			
			session.skaianetHandler.predefineData(player).ifPresent(data -> {
				if(data.getTerrainLandType() != null)
					types.add(data.getTerrainLandType());
			});
		}
		
		return types;
	}
	
	private static <T> T getRandomFromList(List<T> list, Random random)
	{
		return list.get(random.nextInt(list.size()));
	}
}