package com.mraof.minestuck.world.lands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.tags.TagKey;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod.EventBusSubscriber
public final class LandTypeSelection
{
	private static List<LandsSupplier<TerrainLandType>> terrainList;
	private static Map<EnumAspect, List<LandsSupplier<TitleLandType>>> titleByAspect;
	
	@SubscribeEvent
	public static void onServerStart(ServerStartingEvent event)
	{
		{
			ImmutableList.Builder<LandsSupplier<TerrainLandType>> builder = ImmutableList.builder();
			builder.add(of(MSTags.TerrainLandTypes.FOREST));
			builder.add(of(LandTypes.FROST));
			builder.add(of(LandTypes.FUNGI));
			builder.add(of(LandTypes.HEAT));
			builder.add(of(MSTags.TerrainLandTypes.ROCK));
			builder.add(of(MSTags.TerrainLandTypes.SAND));
			builder.add(of(MSTags.TerrainLandTypes.SANDSTONE));
			builder.add(of(LandTypes.SHADE));
			builder.add(of(LandTypes.WOOD));
			builder.add(of(LandTypes.RAINBOW));
			builder.add(of(LandTypes.FLORA));
			builder.add(of(LandTypes.END));
			terrainList = builder.build();
		}
		{
			ImmutableMap.Builder<EnumAspect, List<LandsSupplier<TitleLandType>>> builder = ImmutableMap.builder();
			builder.put(EnumAspect.BLOOD, List.of(of(LandTypes.PULSE)));
			builder.put(EnumAspect.BREATH, List.of(of(LandTypes.WIND)));
			builder.put(EnumAspect.DOOM, List.of(of(LandTypes.THUNDER)));
			builder.put(EnumAspect.HEART, List.of(of(LandTypes.CAKE)));
			builder.put(EnumAspect.HOPE, List.of(of(LandTypes.TOWERS)));
			builder.put(EnumAspect.LIFE, List.of(of(LandTypes.RABBITS)));
			builder.put(EnumAspect.LIGHT, List.of(of(LandTypes.LIGHT)));
			builder.put(EnumAspect.MIND, List.of(of(LandTypes.THOUGHT)));
			builder.put(EnumAspect.RAGE, List.of(of(MSTags.TitleLandTypes.MONSTERS)));
			builder.put(EnumAspect.SPACE, List.of(of(LandTypes.BUCKETS)));
			builder.put(EnumAspect.TIME, List.of(of(LandTypes.CLOCKWORK)));
			builder.put(EnumAspect.VOID, List.of(of(LandTypes.SILENCE)));
			
			titleByAspect = builder.build();
		}
	}
	
	public static Collection<List<TerrainLandType>> terrainAlternatives()
	{
		return terrainList.stream().map(LandsSupplier::get).toList();
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
	
	private static <A> LandsSupplier<A> of(Supplier<A> landType)
	{
		return new LandList<>(Collections.singletonList(landType.get()));
	}
	
	private static <A> LandsSupplier<A> of(TagKey<A> tag)
	{
		return new LandTag<>(tag);
	}
	
	private interface LandsSupplier<A>
	{
		List<A> get();
		
		default boolean contains(A element)
		{
			return this.get().contains(element);
		}
	}
	
	private record LandList<A>(List<A> landTypes) implements LandsSupplier<A>
	{
		@Override
		public List<A> get()
		{
			return this.landTypes;
		}
	}
	
	private record LandTag<A>(TagKey<A> tag) implements LandsSupplier<A>
	{
		@Override
		public List<A> get()
		{
			ForgeRegistry<A> registry;
			if(tag.registry().equals(LandTypes.TERRAIN_KEY))
				registry = (ForgeRegistry<A>) LandTypes.TERRAIN_REGISTRY.get();
			else if(tag.registry().equals(LandTypes.TITLE_KEY))
				registry = (ForgeRegistry<A>) LandTypes.TITLE_REGISTRY.get();
			else
				throw new RuntimeException("Has tag for unhandled registry: " + this.tag.registry());
			
			return Objects.requireNonNull(registry.tags()).getTag(this.tag).stream().toList();
		}
	}
}
