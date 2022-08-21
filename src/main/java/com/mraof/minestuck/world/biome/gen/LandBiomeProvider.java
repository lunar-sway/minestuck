package com.mraof.minestuck.world.biome.gen;


import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mraof.minestuck.world.biome.ILandBiomeSet;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandGenSettings;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import java.util.List;

public class LandBiomeProvider extends BiomeSource
{
	public static final Codec<LandBiomeProvider> CODEC = Codec.of(Encoder.error("LandBiomeProvider is not serializable."), Decoder.error("LandBiomeProvider is not serializable."));
	
	private final ILandBiomeSet biomes;
	private final LandGenSettings settings;
	private final Climate.ParameterList<Holder<Biome>> parameters;
	
	public LandBiomeProvider(long seed, ILandBiomeSet biomes, LandGenSettings settings)
	{
		super(biomes.getAll());
		this.biomes = biomes;
		this.settings = settings;
		
		this.parameters = new Climate.ParameterList<>(List.of(
				Pair.of(Climate.parameters(0, 0, -0.3F, 0, 0, 0, 0), biomes.fromType(LandBiomeType.OCEAN)),
				Pair.of(Climate.parameters(0, 0, -0.1F, 0, 0, 0, 0), biomes.fromType(LandBiomeType.NORMAL))
		));
	}
	
	@Override
	public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
		return parameters.findValue(sampler.sample(x, y, z));
	}
	
	@Override
	protected Codec<? extends BiomeSource> codec()
	{
		return CODEC;
	}
	
	@Override
	public BiomeSource withSeed(long seed)
	{
		return new LandBiomeProvider(seed, biomes, settings);
	}
}