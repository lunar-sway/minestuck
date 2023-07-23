package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public final class BiomeModifierProvider
{
	public static void register(BootstapContext<BiomeModifier> context)
	{
		HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		
		context.register(key("overworld_ores"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
				biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
				HolderSet.direct(features.getOrThrow(MSPlacedFeatures.CRUXITE_ORE), features.getOrThrow(MSPlacedFeatures.URANIUM_ORE)),
				GenerationStep.Decoration.UNDERGROUND_ORES));
	}
	
	private static ResourceKey<BiomeModifier> key(String path)
	{
		return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Minestuck.MOD_ID, path));
	}
}
