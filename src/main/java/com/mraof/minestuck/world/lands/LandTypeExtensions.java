package com.mraof.minestuck.world.lands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Holds all land type extensions loaded from datapacks,
 * and is used to apply these extensions to biome property builders based on land types.
 * An instance can be obtained with {@link LandTypeExtensions#get()}
 * after the server has started and datapacks have been initially loaded.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class LandTypeExtensions
{
	private static final Logger LOGGER = LogManager.getLogger();
	public static final FileToIdConverter TERRAIN_EXTENSIONS_LOCATION = FileToIdConverter.json("minestuck/land_type_extension/terrain");
	public static final FileToIdConverter TITLE_EXTENSIONS_LOCATION = FileToIdConverter.json("minestuck/land_type_extension/title");
	
	private final Map<ILandType, List<Extension>> extensionsMap;
	
	private LandTypeExtensions(Map<ILandType, List<Extension>> extensionsMap)
	{
		this.extensionsMap = extensionsMap;
	}
	
	public void addBiomeGenExtensions(LandBiomeGenBuilder builder, LandTypePair landTypes)
	{
		this.extensionsMap.getOrDefault(landTypes.getTerrain(), Collections.emptyList())
				.forEach(extension -> extension.addTo(builder));
		this.extensionsMap.getOrDefault(landTypes.getTitle(), Collections.emptyList())
				.forEach(extension -> extension.addTo(builder));
	}
	
	public void addMobSpawnExtensions(MobSpawnSettings.Builder builder, LandBiomeType type, LandTypePair landTypes)
	{
		this.extensionsMap.getOrDefault(landTypes.getTerrain(), Collections.emptyList())
				.forEach(extension -> extension.addTo(builder, type));
		this.extensionsMap.getOrDefault(landTypes.getTitle(), Collections.emptyList())
				.forEach(extension -> extension.addTo(builder, type));
	}
	
	private static LandTypeExtensions instance;
	
	public static LandTypeExtensions get()
	{
		return Objects.requireNonNull(instance, "Tried to get an instance of LandTypeExtensions too early.");
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(new Loader(event.getRegistryAccess()));
	}
	
	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event)
	{
		instance = null;
	}
	
	private static final class Loader extends SimplePreparableReloadListener<Map<ILandType, List<Extension>>>
	{
		private final DynamicOps<JsonElement> ops;
		private final Registry<LevelStem> levelStems;
		
		private Loader(RegistryAccess registryAccess)
		{
			this.ops = RegistryOps.create(JsonOps.INSTANCE, registryAccess);
			this.levelStems = registryAccess.registryOrThrow(Registries.LEVEL_STEM);
		}
		
		@Override
		protected Map<ILandType, List<Extension>> prepare(ResourceManager resourceManager, ProfilerFiller profiler)
		{
			ImmutableMap.Builder<ILandType, List<Extension>> mapBuilder = ImmutableMap.builder();
			
			loadAllExtensionsAt(TERRAIN_EXTENSIONS_LOCATION, LandTypes.TERRAIN_REGISTRY, mapBuilder, resourceManager);
			loadAllExtensionsAt(TITLE_EXTENSIONS_LOCATION, LandTypes.TITLE_REGISTRY, mapBuilder, resourceManager);
			
			return mapBuilder.build();
		}
		
		private void loadAllExtensionsAt(FileToIdConverter location, Registry<? extends ILandType> registry,
										 ImmutableMap.Builder<ILandType, List<Extension>> mapBuilder, ResourceManager resourceManager)
		{
			for(Map.Entry<ResourceLocation, List<Resource>> entry : location.listMatchingResourceStacks(resourceManager).entrySet())
			{
				ResourceLocation id = location.fileToId(entry.getKey());
				ILandType landType = registry.get(id);
				if(landType == null)
				{
					LOGGER.error("Found extension for unknown land type '{}'", id);
					continue;
				}
				
				ImmutableList.Builder<Extension> builder = ImmutableList.builder();
				
				for(Resource resource : entry.getValue())
					loadExtensionsFromResource(resource, id).ifPresent(extensions -> extensions.addAllTo(builder));
				
				mapBuilder.put(landType, builder.build());
			}
		}
		
		private Optional<ParsedExtension> loadExtensionsFromResource(Resource resource, ResourceLocation location)
		{
			try(Reader reader = resource.openAsReader())
			{
				JsonElement json = JsonParser.parseReader(reader);
				return ParsedExtension.CODEC.parse(this.ops, json)
						.resultOrPartial(message -> LOGGER.error("Problem parsing land type extension for {} from {}, reason: {}", location, resource.sourcePackId(), message));
			} catch(IOException ignored)
			{
				return Optional.empty();
			}
		}
		
		@Override
		protected void apply(Map<ILandType, List<Extension>> extensionsMap, ResourceManager resourceManager, ProfilerFiller profiler)
		{
			LandTypeExtensions extensions = new LandTypeExtensions(extensionsMap);
			boolean allSuccess = true;
			for(LevelStem levelStem : this.levelStems)
			{
				if(levelStem.generator() instanceof LandChunkGenerator generator)
					allSuccess &= generator.tryInit(extensions);
			}
			if(!allSuccess)
				LOGGER.warn("Land type extensions were reloaded mid-game. Any changes to land type extensions will only apply to new land dimensions. The game need to be restarted for said changes to apply to existing land dimensions.");
			
			LandTypeExtensions.instance = extensions;
		}
	}
	
	private interface Extension
	{
		default void addTo(LandBiomeGenBuilder builder)
		{}
		
		default void addTo(MobSpawnSettings.Builder builder, LandBiomeType type)
		{}
	}
	
	public record FeatureExtension(GenerationStep.Decoration step, Holder<PlacedFeature> feature,
									List<LandBiomeType> biomeTypes) implements Extension
	{
		static Codec<FeatureExtension> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(FeatureExtension::step),
						PlacedFeature.CODEC.fieldOf("feature").forGetter(FeatureExtension::feature),
						LandBiomeType.CODEC.listOf().fieldOf("biome_types").forGetter(FeatureExtension::biomeTypes)
				).apply(instance, FeatureExtension::new));
		
		@Override
		public void addTo(LandBiomeGenBuilder builder)
		{
			builder.addFeature(this.step, this.feature, this.biomeTypes.toArray(new LandBiomeType[0]));
		}
	}
	
	public record CarverExtension(GenerationStep.Carving step, Holder<ConfiguredWorldCarver<?>> carver,
								   List<LandBiomeType> biomeTypes) implements Extension
	{
		static Codec<CarverExtension> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						GenerationStep.Carving.CODEC.fieldOf("step").forGetter(CarverExtension::step),
						ConfiguredWorldCarver.CODEC.fieldOf("carver").forGetter(CarverExtension::carver),
						LandBiomeType.CODEC.listOf().fieldOf("biome_types").forGetter(CarverExtension::biomeTypes)
				).apply(instance, CarverExtension::new));
		
		@Override
		public void addTo(LandBiomeGenBuilder builder)
		{
			builder.addCarver(this.step, this.carver, this.biomeTypes.toArray(new LandBiomeType[0]));
		}
	}
	
	public record MobSpawnExtension(MobCategory category, MobSpawnSettings.SpawnerData spawnerData,
									 List<LandBiomeType> biomeTypes) implements Extension
	{
		static Codec<MobSpawnExtension> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						MobCategory.CODEC.fieldOf("category").forGetter(MobSpawnExtension::category),
						MobSpawnSettings.SpawnerData.CODEC.fieldOf("spawner_data").forGetter(MobSpawnExtension::spawnerData),
						LandBiomeType.CODEC.listOf().fieldOf("biome_types").forGetter(MobSpawnExtension::biomeTypes)
				).apply(instance, MobSpawnExtension::new));
		
		@Override
		public void addTo(MobSpawnSettings.Builder builder, LandBiomeType type)
		{
			if(this.biomeTypes.contains(type))
				builder.addSpawn(this.category, this.spawnerData);
		}
	}
	
	public record ParsedExtension(List<FeatureExtension> features, List<CarverExtension> carvers, List<MobSpawnExtension> mobSpawns)
	{
		public static Codec<ParsedExtension> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						ExtraCodecs.strictOptionalField(FeatureExtension.CODEC.listOf(), "features", Collections.emptyList()).forGetter(ParsedExtension::features),
						ExtraCodecs.strictOptionalField(CarverExtension.CODEC.listOf(), "carvers", Collections.emptyList()).forGetter(ParsedExtension::carvers),
						ExtraCodecs.strictOptionalField(MobSpawnExtension.CODEC.listOf(), "mob_spawns", Collections.emptyList()).forGetter(ParsedExtension::mobSpawns)
				).apply(instance, ParsedExtension::new));
		
		void addAllTo(ImmutableList.Builder<Extension> builder)
		{
			builder.addAll(this.features());
			builder.addAll(this.carvers());
			builder.addAll(this.mobSpawns());
		}
	}
}
