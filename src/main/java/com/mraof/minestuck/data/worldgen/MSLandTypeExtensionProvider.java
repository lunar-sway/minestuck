package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypeExtensions;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
	
	@Override
	public final CompletableFuture<?> run(CachedOutput cache)
	{
		return registries.thenCompose(provider -> {
			addExtensions(provider);
			
			List<CompletableFuture<?>> futures = new ArrayList<>(extensionsMap.size());
			
			for(Map.Entry<ResourceLocation, LandTypeExtensions.ParsedExtension> entry : extensionsMap.entrySet())
			{
				Path path = getPath(entry.getKey().getPath());
				futures.add(DataProvider.saveStable(cache, provider, LandTypeExtensions.ParsedExtension.CODEC, entry.getValue(), path));
			}
			
			return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
		});
	}
	
	private void addExtensions(HolderLookup.Provider provider)
	{
		LandTypes.TERRAIN_REGISTRY.entrySet().forEach(entry ->
				{
					TerrainLandType landType = entry.getValue();
					StructureBlockRegistry blockRegistry = new StructureBlockRegistry();
					landType.registerBlocks(blockRegistry);
					HolderLookup.RegistryLookup<PlacedFeature> features = provider.lookupOrThrow(Registries.PLACED_FEATURE);
					extensionsMap.put(entry.getKey().location(), landType.getExtensions(features, blockRegistry));
				}
		);
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
