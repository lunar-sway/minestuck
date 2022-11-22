package com.mraof.minestuck.world.biome;


import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mraof.minestuck.world.gen.LandGenSettings;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

public final class LandBiomeSource extends BiomeSource
{
	public static final Codec<LandBiomeSource> CODEC = Codec.of(Encoder.error("LandBiomeProvider is not serializable."), Decoder.error("LandBiomeProvider is not serializable."));
	
	private final Climate.ParameterList<Holder<Biome>> parameters;
	
	public LandBiomeSource(LandBiomeAccess biomes, LandGenSettings settings)
	{
		super(biomes.getAll());
		
		this.parameters = settings.createBiomeParameters(biomes);
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
		return this;
	}
}