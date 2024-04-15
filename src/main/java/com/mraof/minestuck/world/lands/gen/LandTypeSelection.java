package com.mraof.minestuck.world.lands.gen;

import com.mojang.datafixers.util.Either;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Keeps track of which land types are available for random choice and under which circumstances they are available.
 * Loaded from json by {@link LandTypeSelectionLoader}.
 * See "data/minestuck/minestuck/terrain_land_types.json" and "data/minestuck/minestuck/title_land_types.json".
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class LandTypeSelection
{
	private static List<Group<TerrainLandType>> terrainGroups;
	private static Map<EnumAspect, List<Group<TitleLandType>>> titleByAspect;
	
	static void setData(List<Group<TerrainLandType>> terrainGroups, Map<EnumAspect, List<Group<TitleLandType>>> titleByAspect)
	{
		LandTypeSelection.terrainGroups = terrainGroups;
		LandTypeSelection.titleByAspect = titleByAspect;
	}
	
	public static Collection<List<TerrainLandType>> terrainAlternatives()
	{
		return terrainGroups.stream().map(Group::getEntries).toList();
	}
	
	public static Collection<List<TitleLandType>> titleAlternatives(EnumAspect aspect)
	{
		return titleByAspect.get(aspect).stream().map(Group::getEntries).toList();
	}
	
	public static Set<TitleLandType> compatibleTitleTypes(TerrainLandType terrainType)
	{
		return titleByAspect.values().stream()
				.flatMap(list ->
						list.stream().flatMap(group ->
								group.getEntries().stream().filter(titleType ->
										titleType.isAspectCompatible(terrainType))))
				.collect(Collectors.toSet());
	}
	
	public static Set<TitleLandType> compatibleTitleTypes(TerrainLandType terrainType, EnumAspect aspect)
	{
		return titleByAspect.get(aspect).stream()
				.flatMap(group ->
						group.getEntries().stream().filter(titleType ->
								titleType.isAspectCompatible(terrainType)))
				.collect(Collectors.toSet());
	}
	
	public static Set<EnumAspect> compatibleAspects(TitleLandType titleType)
	{
		return titleByAspect.entrySet().stream()
				.flatMap(entry ->
						entry.getValue().stream().anyMatch(supplier -> supplier.contains(titleType))
								? Stream.of(entry.getKey())
								: Stream.empty())
				.collect(Collectors.toSet());
	}
	
	/**
	 * A group of land types. Land types in the same group will be considered equal when randomly selecting land types,
	 * i.e. when prioritizing unused or less used land types.
	 */
	record Group<A>(Either<List<A>, TagKey<A>> entries)
	{
		public static <A> Group<A> of(List<A> list)
		{
			return new Group<>(Either.left(list));
		}
		
		public static <A> Group<A> of(TagKey<A> tag)
		{
			return new Group<>(Either.right(tag));
		}
		
		public List<A> getEntries()
		{
			return this.entries.map(list -> list,
					tag -> {
						Registry<A> registry = (Registry<A>) BuiltInRegistries.REGISTRY.get(tag.registry().location());
						return registry.getTag(tag).stream().flatMap(set -> set.stream().map(Holder::value)).toList();
					});
		}
		
		public boolean contains(A landType)
		{
			return this.getEntries().contains(landType);
		}
	}
}
