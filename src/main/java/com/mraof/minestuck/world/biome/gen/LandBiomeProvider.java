package com.mraof.minestuck.world.biome.gen;


import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mraof.minestuck.world.biome.ILandBiomeSet;
import com.mraof.minestuck.world.gen.LandGenSettings;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;

public class LandBiomeProvider extends BiomeProvider
{
	public static final Codec<LandBiomeProvider> CODEC = Codec.of(Encoder.error("LandBiomeProvider is not serializable."), Decoder.error("LandBiomeProvider is not serializable."));
	private final LandBiomeLayer genLevelLayer;
	private final ILandBiomeSet biomes;
	private final LandGenSettings settings;
	
	public LandBiomeProvider(long seed, ILandBiomeSet biomes, LandGenSettings settings)
	{
		super(biomes.getAll());
		this.biomes = biomes;
		this.settings = settings;

		this.genLevelLayer = LandBiomeLayer.buildLandProcedure(seed, biomes, settings.oceanChance, settings.roughChance);
	}
	
	@Override
	public Biome getNoiseBiome(int x, int y, int z) {
		return this.genLevelLayer.get(x, z);
	}
	
	@Override
	protected Codec<? extends BiomeProvider> codec()
	{
		return CODEC;
	}
	
	@Override
	public BiomeProvider withSeed(long seed)
	{
		return new LandBiomeProvider(seed, biomes, settings);
	}
}