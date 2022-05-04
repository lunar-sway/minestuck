package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A more specific name is probably good, if anyone has any suggestion.
 * Handles generation of things like land types and titles, which usually depend on what has already been generated in the session.
 * Only meant to be used skaianet-internally during entry preparations and during predefine data.
 * @author Kirderf1
 */
public class Generator
{
	public static final String NO_AVAILABLE_TITLES = "minestuck.skaianet.no_available_titles";
	
	static boolean isTitleValid(Session session, Title title)
	{
		return !session.isTitleUsed(title);
	}
	
	static Title generateTitle(Session session, Set<EnumAspect> availableAspects, PlayerIdentifier ignore) throws SkaianetException
	{
		Random random = new Random();	//TODO seed based on player and world in a good way
		
		Set<Title> usedTitles = session.getUsedTitles(ignore);
		
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
			EnumClass c = getRandomFromList(EnumClass.valuesStream().collect(Collectors.toList()), random);
			
			return new Title(c, a);
		} else if(!unusedClasses.isEmpty())
		{
			EnumAspect a = getRandomFromList(new ArrayList<>(availableAspects), random);
			EnumClass c = getRandomFromList(unusedClasses, random);
			
			return new Title(c, a);
		} else
		{
			List<Title> unusedTitles = availableAspects.stream().flatMap(a -> EnumClass.valuesStream().map(c -> new Title(c, a)))
					.filter(title -> !usedTitles.contains(title)).collect(Collectors.toList());
			
			if(!unusedTitles.isEmpty())
				return getRandomFromList(unusedTitles, random);
			else throw new SkaianetException(NO_AVAILABLE_TITLES);
		}
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
		LandTypes landGen = new LandTypes(random.nextLong());	//TODO seed based on player and world in a good way
		
		List<TitleLandType> usedTypes = session.getUsedTitleLandTypes(ignore);
		
		boolean hasFrogs = usedTypes.contains(LandTypes.FROGS);
		
		//if(aspect == EnumAspect.SPACE && !hasFrogs)
		//Maybe force it if we so want?
		
		return landGen.getTitleAspect(terrainType, aspect, usedTypes);
	}
	
	static TerrainLandType generateWeightedTerrainLandType(Session session, TitleLandType titleType, PlayerIdentifier ignore)
	{
		Random random = new Random();
		LandTypes landGen = new LandTypes(random.nextLong());	//TODO seed based on player and world in a good way
		
		List<TerrainLandType> usedTypes = session.getUsedTerrainLandTypes(ignore);
		
		return landGen.getTerrainAspect(titleType, usedTypes);
	}
	
	private static <T> T getRandomFromList(List<T> list, Random random)
	{
		return list.get(random.nextInt(list.size()));
	}
}