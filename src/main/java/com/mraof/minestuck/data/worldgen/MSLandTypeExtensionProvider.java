package com.mraof.minestuck.data.worldgen;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypeExtensions;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

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
	private final Map<ResourceLocation, LandTypeExtensions.ParsedExtension> extensionsMap = new HashMap<>();
	
	public MSLandTypeExtensionProvider(PackOutput output)
	{
		this.output = output;
	}
	
	protected void addExtensions()
	{
		LandTypes.TERRAIN_REGISTRY.entrySet().forEach(entry ->
				{
					TerrainLandType landType = entry.getValue();
					StructureBlockRegistry blockRegistry = new StructureBlockRegistry();
					landType.registerBlocks(blockRegistry);
					extensionsMap.put(entry.getKey().location(), landType.getExtensions(blockRegistry));
				}
		);
	}
	
	@Override
	public final CompletableFuture<?> run(CachedOutput cache)
	{
		addExtensions();
		List<CompletableFuture<?>> futures = new ArrayList<>(extensionsMap.size());
		
		for(Map.Entry<ResourceLocation, LandTypeExtensions.ParsedExtension> entry : extensionsMap.entrySet())
		{
			Path path = getPath(entry.getKey().getPath());
			JsonElement jsonData = LandTypeExtensions.ParsedExtension.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
			futures.add(DataProvider.saveStable(cache, jsonData, path));
		}
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
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
