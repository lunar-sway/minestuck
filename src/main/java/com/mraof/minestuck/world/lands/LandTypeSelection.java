package com.mraof.minestuck.world.lands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class LandTypeSelection extends SimplePreparableReloadListener<LandTypeSelection.RawData>
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String TERRAIN_PATH = "minestuck/terrain_land_types.json";
	
	private static List<LandsSupplier<TerrainLandType>> terrainList;
	private static Map<EnumAspect, List<LandsSupplier<TitleLandType>>> titleByAspect;
	
	@SubscribeEvent
	public static void onServerStart(ServerStartingEvent event)
	{
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
		return of(landType.get());
	}
	
	private static <A> LandsSupplier<A> of(A landType)
	{
		return new LandList<>(Collections.singletonList(landType));
	}
	
	private static <A> LandsSupplier<A> of(TagKey<A> tag)
	{
		return new LandTag<>(tag);
	}
	
	public interface LandsSupplier<A>
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
	
	public record RawData(List<GroupData> terrainTypeData)
	{}
	
	private record GroupData(Either<List<ResourceLocation>, ExtraCodecs.TagOrElementLocation> value)
	{
		private <A> Optional<LandsSupplier<A>> lookup(IForgeRegistry<A> registry)
		{
			return this.value.map(list -> {
				List<A> elements = new ArrayList<>();
				for(ResourceLocation id : list)
					getOrLog(registry, id).ifPresent(elements::add);
				
				if(elements.isEmpty())
					return Optional.empty();
				else
					return Optional.of(new LandList<>(elements));
			}, location -> {
				if(location.tag())
					return Optional.of(of(TagKey.create(registry.getRegistryKey(), location.id())));
				else
					return getOrLog(registry, location.id()).map(LandTypeSelection::of);
			});
		}
		
		private static <A> Optional<A> getOrLog(IForgeRegistry<A> registry, ResourceLocation id)
		{
			A element = registry.getValue(id);
			if(element == null)
				LOGGER.error("Could not find land type for id in registry {}: {}", registry.getRegistryKey(), id);
			return Optional.ofNullable(element);
		}
	}
	
	private static final Codec<GroupData> GROUP_DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(new ExtraCodecs.EitherCodec<>(ResourceLocation.CODEC.listOf(), ExtraCodecs.TAG_OR_ELEMENT_ID).fieldOf("value").forGetter(GroupData::value)).apply(instance, GroupData::new));
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(new LandTypeSelection());
	}
	
	@Override
	protected RawData prepare(ResourceManager resourceManager, ProfilerFiller profiler)
	{
		List<GroupData> terrainData = new ArrayList<>();
		
		for(String namespace : resourceManager.getNamespaces())
		{
			ResourceLocation location = new ResourceLocation(namespace, TERRAIN_PATH);
			resourceManager.getResource(location).ifPresent(resource -> {
				try(Reader reader = resource.openAsReader())
				{
					JsonElement json = JsonParser.parseReader(reader);
					List<GroupData> parsedData = GROUP_DATA_CODEC.listOf().parse(JsonOps.INSTANCE, json).getOrThrow(false, LOGGER::error);
					terrainData.addAll(parsedData);
				} catch(IOException ignored)
				{
				} catch(RuntimeException runtimeexception)
				{
					LOGGER.warn("Invalid json in data pack: '{}'", location.toString(), runtimeexception);
				}
			});
		}
		
		return new RawData(terrainData);
	}
	
	@Override
	protected void apply(RawData data, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		ImmutableList.Builder<LandsSupplier<TerrainLandType>> terrainSuppliers = ImmutableList.builder();
		
		for(GroupData terrainGroup : data.terrainTypeData())
			terrainGroup.lookup(LandTypes.TERRAIN_REGISTRY.get()).ifPresent(terrainSuppliers::add);
		
		terrainList = terrainSuppliers.build();
	}
}
