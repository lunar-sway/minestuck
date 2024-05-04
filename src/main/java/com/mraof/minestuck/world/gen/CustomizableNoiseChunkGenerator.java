package com.mraof.minestuck.world.gen;

import com.google.common.base.Suppliers;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An extension to {@link NoiseBasedChunkGenerator} designed to allow use of a second biome source used exclusively for worldgen,
 * which can be allowed to provide biomes outside the biome registry, such as dimension-specific biomes for land dimensions.
 */
public abstract class CustomizableNoiseChunkGenerator extends NoiseBasedChunkGenerator
{
	public CustomizableNoiseChunkGenerator(BiomeSource biomeSource, Function<Holder<Biome>, BiomeGenerationSettings> settingsGetter, Holder<NoiseGeneratorSettings> settingsHolder)
	{
		super(biomeSource, settingsHolder);
		
		setWorldgenSettingsGetter(settingsGetter);
	}
	
	/**
	 * {@link NoiseBasedChunkGenerator} only accepts one biome source,
	 * which will then be used as the source for both worldgen biomes and registry-backed biomes.
	 * We deal with it by changing the worldgen biome source through reflection afterwards.
	 */
	private void setWorldgenSettingsGetter(Function<Holder<Biome>, BiomeGenerationSettings> settingsGetter)
	{
		ObfuscationReflectionHelper.setPrivateValue(ChunkGenerator.class, this, settingsGetter, "generationSettingsGetter");
		
		@SuppressWarnings("deprecation")
		Supplier<List<FeatureSorter.StepFeatureData>> featuresSupplier = Suppliers.memoize(() -> FeatureSorter.buildFeaturesPerStep(
				List.copyOf(this.getBiomeSource().possibleBiomes()), biome -> getBiomeGenerationSettings(biome).features(), true));
		ObfuscationReflectionHelper.setPrivateValue(ChunkGenerator.class, this, featuresSupplier, "featuresPerStep");
	}
}
