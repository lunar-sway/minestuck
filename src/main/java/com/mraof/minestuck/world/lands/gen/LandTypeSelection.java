package com.mraof.minestuck.world.lands.gen;

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
	private static List<LandsSupplier<TerrainLandType>> terrainGroups;
	private static Map<EnumAspect, List<LandsSupplier<TitleLandType>>> titleByAspect;
	
	static void setData(List<LandsSupplier<TerrainLandType>> terrainGroups, Map<EnumAspect, List<LandsSupplier<TitleLandType>>> titleByAspect)
	{
		LandTypeSelection.terrainGroups = terrainGroups;
		LandTypeSelection.titleByAspect = titleByAspect;
	}
	
	public static Collection<List<TerrainLandType>> terrainAlternatives()
	{
		return terrainGroups.stream().map(LandsSupplier::get).toList();
	}
	
	public static Collection<List<TitleLandType>> titleAlternatives(EnumAspect aspect)
	{
		return titleByAspect.get(aspect).stream().map(LandsSupplier::get).toList();
	}
	
	public static Set<TitleLandType> compatibleTitleTypes(TerrainLandType terrainType)
	{
		return titleByAspect.values().stream()
				.flatMap(list ->
						list.stream().flatMap(supplier ->
								supplier.get().stream().filter(titleType ->
										titleType.isAspectCompatible(terrainType))))
				.collect(Collectors.toSet());
	}
	
	public static Set<TitleLandType> compatibleTitleTypes(TerrainLandType terrainType, EnumAspect aspect)
	{
		return titleByAspect.get(aspect).stream()
				.flatMap(supplier ->
						supplier.get().stream().filter(titleType ->
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
	
	sealed interface LandsSupplier<A> permits LandList, LandTag
	{
		List<A> get();
		
		default boolean contains(A element)
		{
			return this.get().contains(element);
		}
		
	}
	
	record LandList<A>(List<A> landTypes) implements LandsSupplier<A>
	{
		@Override
		public List<A> get()
		{
			return this.landTypes;
		}
		
	}
	
	record LandTag<A>(TagKey<A> tag) implements LandsSupplier<A>
	{
		@Override
		public List<A> get()
		{
			IForgeRegistry<A> registry = RegistryManager.ACTIVE.getRegistry(this.tag.registry());
			
			return Objects.requireNonNull(registry.tags()).getTag(this.tag).stream().toList();
		}
	}
}
