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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

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
		private <A> Optional<LandTypeSelection.Group<A>> lookup(IForgeRegistry<A> registry)
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
					TagKey<A> tag = TagKey.create(registry.getRegistryKey(), location.id());
					return Optional.of(LandTypeSelection.Group.of(tag));
				}
				else
					return getOrLog(registry, location.id()).map(landType -> LandTypeSelection.Group.of(Collections.singletonList(landType)));
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
			ResourceLocation location = new ResourceLocation(namespace, TERRAIN_PATH);
			resourceManager.getResource(location).ifPresent(resource -> {
				try(Reader reader = resource.openAsReader())
				{
					JsonElement json = JsonParser.parseReader(reader);
					List<GroupData> parsedData = TERRAIN_DATA_CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, LOGGER::error);
					terrainData.addAll(parsedData);
				} catch(IOException ignored)
				{
				} catch(RuntimeException runtimeexception)
				{
					LOGGER.warn("Invalid json in data pack: '{}'", location.toString(), runtimeexception);
				}
			});
		}
		
		Map<EnumAspect, List<GroupData>> titleData = new HashMap<>();
		for(EnumAspect aspect : EnumAspect.values())
			titleData.put(aspect, new ArrayList<>());
		
		for(String namespace : resourceManager.getNamespaces())
		{
			ResourceLocation location = new ResourceLocation(namespace, TITLE_PATH);
			resourceManager.getResource(location).ifPresent(resource -> {
				try(Reader reader = resource.openAsReader())
				{
					JsonElement json = JsonParser.parseReader(reader);
					Map<EnumAspect, List<GroupData>> parsedData = TITLE_DATA_CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, LOGGER::error);
					for(EnumAspect aspect : parsedData.keySet())
						titleData.get(aspect).addAll(parsedData.get(aspect));
					
				} catch(IOException ignored)
				{
				} catch(RuntimeException runtimeexception)
				{
					LOGGER.warn("Invalid json in data pack: '{}'", location.toString(), runtimeexception);
				}
			});
		}
		
		return new RawData(terrainData, titleData);
	}
	
	@Override
	protected void apply(RawData data, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		ImmutableList.Builder<LandTypeSelection.Group<TerrainLandType>> terrainGroupsBuilder = ImmutableList.builder();
		
		for(GroupData terrainGroup : data.terrainTypeData())
			terrainGroup.lookup(LandTypes.TERRAIN_REGISTRY.get()).ifPresent(terrainGroupsBuilder::add);
		
		
		ImmutableMap.Builder<EnumAspect, List<LandTypeSelection.Group<TitleLandType>>> titleMapBuilder = ImmutableMap.builder();
		
		for(EnumAspect aspect : EnumAspect.values())
		{
			ImmutableList.Builder<LandTypeSelection.Group<TitleLandType>> titleGroupsBuilder = ImmutableList.builder();
			
			for(GroupData titleGroup : data.titleTypeData().get(aspect))
				titleGroup.lookup(LandTypes.TITLE_REGISTRY.get()).ifPresent(titleGroupsBuilder::add);
			
			titleMapBuilder.put(aspect, titleGroupsBuilder.build());
		}
		
		LandTypeSelection.setData(terrainGroupsBuilder.build(), titleMapBuilder.build());
	}
}