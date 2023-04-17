package com.mraof.minestuck.data.worldgen;

import com.google.gson.JsonElement;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class BiomeModifierProvider
{
	public static DataProvider create(DataGenerator generator, ExistingFileHelper existingFileHelper, RegistryOps<JsonElement> registryOps)
	{
		DataEntriesBuilder<BiomeModifier> entries = new DataEntriesBuilder<>();
		register(registryOps.registryAccess, entries.consumerForNamespace(Minestuck.MOD_ID));
		
		return JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, Minestuck.MOD_ID,
				registryOps, ForgeRegistries.Keys.BIOME_MODIFIERS, entries.getMap());
	}
	
	private static void register(RegistryAccess registries, BiConsumer<String, BiomeModifier> builder)
	{
		Registry<PlacedFeature> features = registries.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY);
		Registry<Biome> biomes = registries.registryOrThrow(Registry.BIOME_REGISTRY);
		
		builder.accept("overworld_ores", new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
				biomes.getOrCreateTag(BiomeTags.IS_OVERWORLD),
				HolderSet.direct(features.getHolderOrThrow(MSPlacedFeatures.CRUXITE_ORE), features.getHolderOrThrow(MSPlacedFeatures.URANIUM_ORE)),
				GenerationStep.Decoration.UNDERGROUND_ORES));
	}
}
