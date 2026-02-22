package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypeExtensions;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MSLandTypeExtensionProvider implements DataProvider
{
	private final PackOutput output;
	private final CompletableFuture<HolderLookup.Provider> registries;
	private final Map<ResourceLocation, LandTypeExtensions.ParsedExtension> extensionsMap = new HashMap<>();
	
	public MSLandTypeExtensionProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		this.output = output;
		this.registries = registries;
	}
	
	protected void addExtensions()
	{
		try
		{
			//HolderLookup.RegistryLookup<PlacedFeature> featureLookup = registries.get().lookup(Registries.PLACED_FEATURE).orElseThrow();
			//HolderGetter<PlacedFeature> features = registries.get().asGetterLookup().lookupOrThrow(Registries.PLACED_FEATURE);
			Registry<PlacedFeature> features = registries.get().holderOrThrow(Registries.PLACED_FEATURE).value();
			
			LandTypes.TERRAIN_REGISTRY.entrySet().forEach(entry ->
					{
						TerrainLandType landType = entry.getValue();
						StructureBlockRegistry blockRegistry = new StructureBlockRegistry();
						landType.registerBlocks(blockRegistry);
						extensionsMap.put(entry.getKey().location(), landType.getExtensions(features, blockRegistry));
					}
			);
		} catch(InterruptedException | ExecutionException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public final CompletableFuture<?> run(CachedOutput cache)
	{
		return this.registries.thenCompose(provider -> {
			Set<ResourceLocation> set = new HashSet<>();
			List<CompletableFuture<?>> futures = new ArrayList<>();
			Consumer<AdvancementHolder> consumer = advancementHolder -> {
				if(!set.add(advancementHolder.id()))
				{
					throw new IllegalStateException("Duplicate advancement " + advancementHolder.id());
				} else
				{
					//Path path = getPath(entry.getKey().getPath());
					//futures.add(DataProvider.saveStable(output, provider, Advancement.CODEC, advancementHolder.value(), path));
					//futures.add(DataProvider.saveStable(cache, jsonData, path));
				}
			};
			
			/*for(AdvancementSubProvider advancementsubprovider : this.subProviders)
			{
				advancementsubprovider.generate(provider, consumer);
			}*/
			
			return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
		});
		
		/*addExtensions();
		List<CompletableFuture<?>> futures = new ArrayList<>(extensionsMap.size());
		
		for(Map.Entry<ResourceLocation, LandTypeExtensions.ParsedExtension> entry : extensionsMap.entrySet())
		{
			Path path = getPath(entry.getKey().getPath());
			
			//causes error "java.lang.IllegalStateException: Can't access registry ResourceKey[minecraft:root / minecraft:block];"
			JsonElement jsonData = LandTypeExtensions.ParsedExtension.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
			
			futures.add(DataProvider.saveStable(cache, jsonData, path));
		}
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));*/
	}
	
	private Path getPath(String id)
	{
		return this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(Minestuck.MOD_ID).resolve("minestuck/land_type_extension/" + id + ".json");
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Base Land Type Extensions";
	}
}
