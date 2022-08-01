package com.mraof.minestuck.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;

import java.util.Optional;
import java.util.stream.IntStream;

public class SkaiaChunkGenerator extends AbstractChunkGenerator
{
	public static final Codec<SkaiaChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> commonCodec(instance).and(instance.group(
					RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter(generator -> generator.noises),
					BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
					Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
					NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings)))
			.apply(instance, instance.stable(SkaiaChunkGenerator::new)));
	
	private final PerlinNoise depthNoise;
	
	public SkaiaChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, BiomeSource provider, long seed, Holder<NoiseGeneratorSettings> settings)
	{
		super(structureSets, noises, Optional.empty(), provider, provider, seed, settings);
		
		depthNoise = PerlinNoise.createLegacyForBlendedNoise(random, IntStream.rangeClosed(-15, 0));
	}
	
	@Override
	protected Codec<? extends ChunkGenerator> codec()
	{
		return CODEC;
	}
	
	@Override
	public ChunkGenerator withSeed(long seed)
	{
		return new SkaiaChunkGenerator(this.structureSets, this.noises, this.biomeSource.withSeed(seed), seed, this.settings);
	}
	
	@Override
	public Climate.Sampler climateSampler()
	{
		return Climate.empty();
	}
	
	@Override
	protected void fillNoiseColumn(double[] column, int sectX, int sectZ)
	{
		double depth = this.depthNoise.getValue(sectX * 200, 10.0D, sectZ * 200, 1.0D, 0.0D, true);
		
		double scaledHeight = (depth * 65535.0D / 12000.0D + 1.0D) / 4D;
		fillNoiseColumn(column, sectX, sectZ, scaledHeight, 96D / 0.1);
	}
}