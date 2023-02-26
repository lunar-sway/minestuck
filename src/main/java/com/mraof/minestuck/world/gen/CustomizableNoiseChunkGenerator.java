package com.mraof.minestuck.world.gen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An extension to {@link NoiseBasedChunkGenerator} designed to allow use of a second biome source used exclusively for worldgen,
 * which can be allowed to provide biomes outside the biome registry, such as dimension-specific biomes for land dimensions.
 */
public abstract class CustomizableNoiseChunkGenerator extends NoiseBasedChunkGenerator
{
	protected final Registry<NormalNoise.NoiseParameters> noises;	//Handy for implementing withSeed() since it has private access in NoiseBasedChunkGenerator
	
	public CustomizableNoiseChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, BiomeSource biomeSource, BiomeSource runtimeBiomeSource, long seed, Holder<NoiseGeneratorSettings> settingsHolder)
	{
		super(structureSets, noises, runtimeBiomeSource, seed, settingsHolder);
		
		setWorldgenBiomeSource(biomeSource);
		
		this.noises = noises;
	}
	
	@Override
	protected abstract Codec<? extends CustomizableNoiseChunkGenerator> codec();
	
	@Override
	public abstract ChunkGenerator withSeed(long seed);
	
	protected Holder<Biome> getWorldGenBiome(Holder<Biome> actualBiome)
	{
		return actualBiome;
	}
	
	/**
	 * A custom implementation that properly works with custom biomes for worldgen by:
	 * a) Making sure to use worldgen biomes instead of the registry-backed biomes to figure out which worldgen features to generate.
	 * b) Making sure that {@link BiomeSource} checks against the worldgen biomes instead of the registry-backed biomes.
	 * Not as optimised as the usual implementation of this function:
	 * Instead of iterating over worldgen features for biomes in the chunk, it iterates over worldgen features for all biomes in the dimension.
	 * This should be fine as long as our number of biomes is relatively low.
	 */
	@Override
	public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureFeatureManager structureManager)
	{
		ChunkPos chunkPos = chunk.getPos();
		SectionPos sectionPos = SectionPos.of(chunkPos, level.getMinSection());
		BlockPos pos = sectionPos.origin();
		BoundingBox writeableBox = new BoundingBox(chunkPos.getMinBlockX(), chunk.getMinBuildHeight() + 1, chunkPos.getMinBlockZ(),
				chunkPos.getMaxBlockX(), chunk.getMaxBuildHeight() - 1, chunkPos.getMaxBlockZ());
		WorldgenRandom random = new WorldgenRandom(new XoroshiroRandomSource(RandomSupport.seedUniquifier()));
		//Override WorldGenLevel.getBiome() in order to control the biome checked in BiomeFilter
		WorldGenLevel customBiomeLevel = new WorldGenLevelWrapper(level)
		{
			@Override
			public Holder<Biome> getBiome(BlockPos pos)
			{
				return CustomizableNoiseChunkGenerator.this.getWorldGenBiome(super.getBiome(pos));
			}
		};
		long decorationSeed = random.setDecorationSeed(level.getSeed(), pos.getX(), pos.getZ());
		
		Registry<ConfiguredStructureFeature<?, ?>> registry = level.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		Map<Integer, List<ConfiguredStructureFeature<?, ?>>> structuresByStep = registry.stream().collect(Collectors.groupingBy(configured -> configured.feature.step().ordinal()));
		
		for(int decorationStep = 0; decorationStep < this.biomeSource.featuresPerStep().size(); decorationStep++)
		{
			if(structureManager.shouldGenerateFeatures())
			{
				List<ConfiguredStructureFeature<?, ?>> configuredStructures = structuresByStep.getOrDefault(decorationStep, Collections.emptyList());
				for(int index = 0; index < configuredStructures.size(); index++)
				{
					random.setFeatureSeed(decorationSeed, index, decorationStep);
					structureManager.startsForFeature(sectionPos, configuredStructures.get(index)).forEach(
							structureStart -> structureStart.placeInChunk(customBiomeLevel, structureManager, this, random, writeableBox, chunkPos));
				}
			}
			
			BiomeSource.StepFeatureData featureStep = this.biomeSource.featuresPerStep().get(decorationStep);
			for(PlacedFeature feature : featureStep.features())
			{
				random.setFeatureSeed(decorationSeed, featureStep.indexMapping().applyAsInt(feature), decorationStep);
				feature.placeWithBiomeCheck(customBiomeLevel, this, random, pos);
			}
		}
	}
	
	/**
	 * {@link NoiseBasedChunkGenerator} only accepts one biome source,
	 * which will then be used as the source for both worldgen biomes and registry-backed biomes.
	 * We deal with it by changing the worldgen biome source through reflection afterwards.
	 */
	private void setWorldgenBiomeSource(BiomeSource biomeSource)
	{
		ObfuscationReflectionHelper.setPrivateValue(ChunkGenerator.class, this, biomeSource, "f_62137_");
	}
}
