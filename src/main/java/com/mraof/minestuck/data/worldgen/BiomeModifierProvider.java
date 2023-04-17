package com.mraof.minestuck.data.worldgen;

import com.google.gson.JsonElement;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class BiomeModifierProvider
{
	public static DataProvider create(DataGenerator generator, ExistingFileHelper existingFileHelper, RegistryOps<JsonElement> registryOps)
	{
		Map<ResourceLocation, BiomeModifier> entries = new HashMap<>();
		register(registryOps.registryAccess, (name, modifier) -> {
			ResourceLocation id = new ResourceLocation(Minestuck.MOD_ID, name);
			if(entries.containsKey(id))
				throw new IllegalArgumentException("Name \"" + name + "\" is already used");
			else
				entries.put(id, modifier);
		});
		return JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, Minestuck.MOD_ID, registryOps, ForgeRegistries.Keys.BIOME_MODIFIERS, entries);
	}
	
	private static void register(RegistryAccess registries, BiConsumer<String, BiomeModifier> builder)
	{
		builder.accept("overworld_ores", new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
				holderSet(registries, BiomeTags.IS_OVERWORLD),
				HolderSet.direct(holder(registries, MSPlacedFeatures.CRUXITE_ORE), holder(registries, MSPlacedFeatures.URANIUM_ORE)),
				GenerationStep.Decoration.UNDERGROUND_ORES));
	}
	
	private static HolderSet<Biome> holderSet(RegistryAccess registries, @SuppressWarnings("SameParameterValue") TagKey<Biome> tag)
	{
		return registries.registry(Registry.BIOME_REGISTRY).orElseThrow().getOrCreateTag(tag);
	}
	
	private static Holder<PlacedFeature> holder(RegistryAccess registries, RegistryObject<PlacedFeature> placed)
	{
		return registries.registry(Registry.PLACED_FEATURE_REGISTRY).orElseThrow().getHolderOrThrow(Objects.requireNonNull(placed.getKey()));
	}
}
