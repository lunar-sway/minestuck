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
				Pair.of(simpleParameterPoint(Climate.Parameter.span(-1, -0.2F), Climate.Parameter.span(-1, 1)), biomes.fromType(LandBiomeType.OCEAN)),
				Pair.of(simpleParameterPoint(Climate.Parameter.span(-0.2F, 1), Climate.Parameter.span(-0.2F, 1)), biomes.fromType(LandBiomeType.NORMAL)),
				Pair.of(simpleParameterPoint(Climate.Parameter.span(-0.2F, 1), Climate.Parameter.span(-1, -0.2F)), biomes.fromType(LandBiomeType.ROUGH))
		));
	}
	
	private static Climate.ParameterPoint simpleParameterPoint(Climate.Parameter continents, Climate.Parameter erosion)
	{
		return Climate.parameters(Climate.Parameter.point(0), Climate.Parameter.point(0), continents, erosion, Climate.Parameter.point(0), Climate.Parameter.point(0), 0);
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