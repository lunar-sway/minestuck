package com.mraof.minestuck.world.gen;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An extension to {@link NoiseBasedChunkGenerator} designed to allow use of a custom biome settings getter for worldgen,
 * which can be allowed to provide biome generation settings different from that of the registry biomes,
 * such as dimension-specific biomes for land dimensions.
 */
public abstract class CustomizableNoiseChunkGenerator extends NoiseBasedChunkGenerator
{
	public CustomizableNoiseChunkGenerator(BiomeSource biomeSource, Function<Holder<Biome>, BiomeGenerationSettings> settingsGetter, Holder<NoiseGeneratorSettings> settingsHolder)
	{
		super(biomeSource, settingsHolder);
		
		setWorldgenSettingsGetter(settingsGetter);
	}
	
	/**
	 * {@link NoiseBasedChunkGenerator} does not allow setting a custom generation settings getter,
	 * and instead uses the biome generation settings of the registry biomes.
	 * We deal with it by changing the generation settings getter through reflection afterwards.
	 */
	private void setWorldgenSettingsGetter(Function<Holder<Biome>, BiomeGenerationSettings> settingsGetter)
	{
		ObfuscationReflectionHelper.setPrivateValue(ChunkGenerator.class, this, settingsGetter, "generationSettingsGetter");
		
		@SuppressWarnings("deprecation")
		Supplier<List<FeatureSorter.StepFeatureData>> featuresSupplier = Lazy.of(() -> FeatureSorter.buildFeaturesPerStep(
				List.copyOf(this.getBiomeSource().possibleBiomes()), biome -> getBiomeGenerationSettings(biome).features(), true));
		ObfuscationReflectionHelper.setPrivateValue(ChunkGenerator.class, this, featuresSupplier, "featuresPerStep");
	}
}
