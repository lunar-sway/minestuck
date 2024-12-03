package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.gen.LandTypeGenerator;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A more specific name is probably good, if anyone has any suggestion.
 * Handles generation of things like land types and titles, which usually depend on what has already been generated in the session.
 * Only meant to be used skaianet-internally during entry preparations and during predefine data.
 * @author Kirderf1
 */
class Generator
{
	static Title generateTitle(PlayerIdentifier player, Set<EnumAspect> availableAspects, SkaianetData skaianetData)
	{
		Random random = new Random();	//TODO seed based on player and world in a good way
		
		Set<Title> usedTitles = titlesUsedBy(skaianetData.sessionHandler.playersToCheckForDataSelection(player).toList(), skaianetData);
		
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
	
	static Set<Title> titlesUsedBy(Collection<PlayerIdentifier> players, SkaianetData skaianetData)
	{
		return players.stream().flatMap(player -> titleForPlayer(player, skaianetData).stream()).collect(Collectors.toSet());
	}
	
	@NotNull
	private static Optional<Title> titleForPlayer(PlayerIdentifier player, SkaianetData skaianetData)
	{
		return Title.getTitle(player, skaianetData.mcServer)
				.or(() -> skaianetData.getOrCreatePredefineData(player).flatMap(data -> Optional.ofNullable(data.getTitle())));
	}
	
	private static List<EnumAspect> unusedAspects(Set<EnumAspect> base, Set<Title> usedTitles)
	{
		Set<EnumAspect> usedAspects = usedTitles.stream().map(Title::heroAspect).collect(Collectors.toSet());
		return base.stream().filter(a -> !usedAspects.contains(a)).collect(Collectors.toList());
	}
	
	private static List<EnumClass> unusedClasses(Set<Title> usedTitles)
	{
		Set<EnumClass> usedClasses = usedTitles.stream().map(Title::heroClass).collect(Collectors.toSet());
		return EnumClass.valuesStream().filter(c -> !usedClasses.contains(c)).collect(Collectors.toList());
	}
	
	static TitleLandType generateWeightedTitleLandType(Collection<PlayerIdentifier> otherPlayers, EnumAspect aspect, @Nullable TerrainLandType terrainType, SkaianetData skaianetData)
	{
		Random random = new Random();
		LandTypeGenerator landGen = new LandTypeGenerator(random.nextLong());	//TODO seed based on player and world in a good way
		
		List<TitleLandType> usedTypes = titleLandTypesUsedBy(otherPlayers, skaianetData);
		
		return landGen.getTitleAspect(terrainType, aspect, usedTypes);
	}
	
	static List<TitleLandType> titleLandTypesUsedBy(Collection<PlayerIdentifier> players, SkaianetData skaianetData)
	{
		return players.stream().flatMap(player -> titleLandTypeForPlayer(player, skaianetData).stream()).toList();
	}
	
	private static Optional<TitleLandType> titleLandTypeForPlayer(PlayerIdentifier player, SkaianetData skaianetData)
	{
		return LandTypePair.getTypes(skaianetData.mcServer, skaianetData.getOrCreateData(player).getLandDimension())
				.map(LandTypePair::getTitle)
				.or(() -> skaianetData.getOrCreatePredefineData(player)
						.flatMap(data -> Optional.ofNullable(data.getTitleLandType())));
	}
	
	static TerrainLandType generateWeightedTerrainLandType(Collection<PlayerIdentifier> otherPlayers, TitleLandType titleType, SkaianetData skaianetData)
	{
		Random random = new Random();
		LandTypeGenerator landGen = new LandTypeGenerator(random.nextLong());	//TODO seed based on player and world in a good way
		
		List<TerrainLandType> usedTypes = terrainLandTypesUsedBy(otherPlayers, skaianetData);
		
		return landGen.getTerrainAspect(titleType, usedTypes);
	}
	
	private static List<TerrainLandType> terrainLandTypesUsedBy(Collection<PlayerIdentifier> players, SkaianetData skaianetData)
	{
		return players.stream().flatMap(player -> terrainLandTypeForPlayer(player, skaianetData).stream()).toList();
	}
	
	private static Optional<TerrainLandType> terrainLandTypeForPlayer(PlayerIdentifier player, SkaianetData skaianetData)
	{
		return LandTypePair.getTypes(skaianetData.mcServer, skaianetData.getOrCreateData(player).getLandDimension())
				.map(LandTypePair::getTerrain)
				.or(() -> skaianetData.getOrCreatePredefineData(player)
						.flatMap(data -> Optional.ofNullable(data.getTerrainLandType())));
	}
	
	private static <T> T getRandomFromList(List<T> list, Random random)
	{
		return list.get(random.nextInt(list.size()));
	}
}