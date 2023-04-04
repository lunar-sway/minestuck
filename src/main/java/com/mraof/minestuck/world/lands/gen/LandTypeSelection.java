package com.mraof.minestuck.world.lands.gen;

import com.mojang.datafixers.util.Either;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
						IForgeRegistry<A> registry = RegistryManager.ACTIVE.getRegistry(tag.registry());
						return Objects.requireNonNull(registry.tags()).getTag(tag).stream().toList();
					});
		}
		
		public boolean contains(A landType)
		{
			return this.getEntries().contains(landType);
		}
	}
}
