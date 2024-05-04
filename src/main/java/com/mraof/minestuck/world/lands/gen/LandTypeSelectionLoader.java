package com.mraof.minestuck.world.lands.gen;

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
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Loads data for {@link LandTypeSelection} from datapack jsons. Jsons are loaded in a way to support both extending and replacing.
 * For example, to extend the terrain land type list, declare the json under your namespace:
 *     "data/<your-namespace>/minestuck/terrain_land_types.json".
 * To replace a list, declare it under the same namespace as the replaced list. To replace minestuck:
 *     "data/minestuck/minestuck/terrain_land_types.json".
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class LandTypeSelectionLoader extends SimplePreparableReloadListener<LandTypeSelectionLoader.RawData>
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String TERRAIN_PATH = "minestuck/terrain_land_types.json";
	public static final String TITLE_PATH = "minestuck/title_land_types.json";
	
	public record RawData(List<GroupData> terrainTypeData, Map<EnumAspect, List<GroupData>> titleTypeData)
	{}
	
	private record GroupData(Either<List<ResourceLocation>, ExtraCodecs.TagOrElementLocation> value)
	{
		private <A> Optional<LandTypeSelection.Group<A>> lookup(Registry<A> registry)
		{
			return this.value.map(list -> {
				List<A> elements = new ArrayList<>();
				for(ResourceLocation id : list)
					getOrLog(registry, id).ifPresent(elements::add);
				
				if(elements.isEmpty())
					return Optional.empty();
				else
					return Optional.of(LandTypeSelection.Group.of(elements));
			}, location -> {
				if(location.tag())
				{
					TagKey<A> tag = TagKey.create(registry.key(), location.id());
					return Optional.of(LandTypeSelection.Group.of(tag));
				}
				else
					return getOrLog(registry, location.id()).map(landType -> LandTypeSelection.Group.of(Collections.singletonList(landType)));
			});
		}
		
		private static <A> Optional<A> getOrLog(Registry<A> registry, ResourceLocation id)
		{
			A element = registry.get(id);
			if(element == null)
				LOGGER.error("Could not find land type for id in registry {}: {}", registry.key(), id);
			return Optional.ofNullable(element);
		}
	}
	
	private static final Codec<GroupData> GROUP_DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(new ExtraCodecs.EitherCodec<>(ResourceLocation.CODEC.listOf(), ExtraCodecs.TAG_OR_ELEMENT_ID).fieldOf("value").forGetter(GroupData::value)).apply(instance, GroupData::new));
	private static final Codec<List<GroupData>> TERRAIN_DATA_CODEC = GROUP_DATA_CODEC.listOf();
	private static final Codec<Map<EnumAspect, List<GroupData>>> TITLE_DATA_CODEC = Codec.unboundedMap(EnumAspect.CODEC, GROUP_DATA_CODEC.listOf());
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(new LandTypeSelectionLoader());
	}
	
	@Override
	protected RawData prepare(ResourceManager resourceManager, ProfilerFiller profiler)
	{
		List<GroupData> terrainData = new ArrayList<>();
		
		for(String namespace : resourceManager.getNamespaces())
		{
			parseTerrainGroupsFromNamespace(resourceManager, namespace)
					.ifPresent(terrainData::addAll);
		}
		
		Map<EnumAspect, List<GroupData>> titleData = new HashMap<>();
		for(EnumAspect aspect : EnumAspect.values())
			titleData.put(aspect, new ArrayList<>());
		
		for(String namespace : resourceManager.getNamespaces())
		{
			parseTitleGroupsFromNamespace(resourceManager, namespace).ifPresent(parsedData -> {
				for(EnumAspect aspect : parsedData.keySet())
					titleData.get(aspect).addAll(parsedData.get(aspect));
			});
		}
		
		return new RawData(terrainData, titleData);
	}
	
	private static Optional<List<GroupData>> parseTerrainGroupsFromNamespace(ResourceManager resourceManager, String namespace)
	{
		return parseCodecDataFromLocation(resourceManager, new ResourceLocation(namespace, TERRAIN_PATH), TERRAIN_DATA_CODEC);
	}
	
	private static Optional<Map<EnumAspect, List<GroupData>>> parseTitleGroupsFromNamespace(ResourceManager resourceManager, String namespace)
	{
		return parseCodecDataFromLocation(resourceManager, new ResourceLocation(namespace, TITLE_PATH), TITLE_DATA_CODEC);
	}
	
	private static <T> Optional<T> parseCodecDataFromLocation(ResourceManager resourceManager, ResourceLocation location, Codec<T> codec)
	{
		return resourceManager.getResource(location).flatMap(resource -> {
			try(Reader reader = resource.openAsReader())
			{
				JsonElement json = JsonParser.parseReader(reader);
				return codec.parse(JsonOps.INSTANCE, json)
						.resultOrPartial(message -> LOGGER.error("Problem parsing json: {}, reason: {}", location, message));
				
			} catch(IOException ignored)
			{
				return Optional.empty();
			}
		});
	}
	
	@Override
	protected void apply(RawData data, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		ImmutableList.Builder<LandTypeSelection.Group<TerrainLandType>> terrainGroupsBuilder = ImmutableList.builder();
		
		for(GroupData terrainGroup : data.terrainTypeData())
			terrainGroup.lookup(LandTypes.TERRAIN_REGISTRY).ifPresent(terrainGroupsBuilder::add);
		
		
		ImmutableMap.Builder<EnumAspect, List<LandTypeSelection.Group<TitleLandType>>> titleMapBuilder = ImmutableMap.builder();
		
		for(EnumAspect aspect : EnumAspect.values())
		{
			ImmutableList.Builder<LandTypeSelection.Group<TitleLandType>> titleGroupsBuilder = ImmutableList.builder();
			
			for(GroupData titleGroup : data.titleTypeData().get(aspect))
				titleGroup.lookup(LandTypes.TITLE_REGISTRY).ifPresent(titleGroupsBuilder::add);
			
			titleMapBuilder.put(aspect, titleGroupsBuilder.build());
		}
		
		LandTypeSelection.setData(terrainGroupsBuilder.build(), titleMapBuilder.build());
	}
}