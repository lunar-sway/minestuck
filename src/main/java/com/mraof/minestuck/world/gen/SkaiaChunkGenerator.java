package com.mraof.minestuck.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.OctavesNoiseGenerator;

import java.util.function.Supplier;
import java.util.stream.IntStream;

public class SkaiaChunkGenerator extends AbstractChunkGenerator
{
	public static final Codec<SkaiaChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
					BiomeProvider.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
					Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
					DimensionSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
			.apply(instance, instance.stable(SkaiaChunkGenerator::new)));
	
	private final OctavesNoiseGenerator depthNoise;
	
	public SkaiaChunkGenerator(BiomeProvider provider, long seed, Supplier<DimensionSettings> settings)
	{
		super(provider, provider, seed, settings);
		
		depthNoise = new OctavesNoiseGenerator(random, IntStream.rangeClosed(-15, 0));
	}
	
	@Override
	protected Codec<? extends ChunkGenerator> codec()
	{
		return CODEC;
	}
	
	@Override
	public ChunkGenerator withSeed(long seed)
	{
		return new SkaiaChunkGenerator(biomeSource.withSeed(seed), seed, settings);
	}
	
	@Override
	protected void fillNoiseColumn(double[] column, int sectX, int sectZ)
	{
		double depth = this.depthNoise.getValue(sectX * 200, 10.0D, sectZ * 200, 1.0D, 0.0D, true);
		
		double scaledHeight = (depth * 65535.0D / 12000.0D + 1.0D) * (17 / 64D);
		double scaleMod = 96D / 0.1;
		fillNoiseColumn(column, sectX, sectZ, scaledHeight, scaleMod);
	}
}