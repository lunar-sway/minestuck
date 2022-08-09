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

import java.util.Optional;

public class SkaiaChunkGenerator extends AbstractChunkGenerator
{
	public static final Codec<SkaiaChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> commonCodec(instance).and(instance.group(
					RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter(generator -> generator.noises),
					BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
					Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
					NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings)))
			.apply(instance, instance.stable(SkaiaChunkGenerator::new)));
	
	public SkaiaChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, BiomeSource provider, long seed, Holder<NoiseGeneratorSettings> settings)
	{
		super(structureSets, noises, Optional.empty(), provider, provider, seed, settings);
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
}