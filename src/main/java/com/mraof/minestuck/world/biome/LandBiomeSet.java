package com.mraof.minestuck.world.biome;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.List;

public class LandBiomeSet implements ILandBiomeSet
{
	public final RegistryObject<Biome> NORMAL, ROUGH, OCEAN;
	private final String name;
	
	public LandBiomeSet(DeferredRegister<Biome> register, String name, Biome.RainType precipitation, float temperature, float downfall)
	{
		this.name = name;
		NORMAL = register.register("land_"+name+"_normal", () -> LandBiome.createNormalBiome(precipitation, temperature, downfall));
		ROUGH = register.register("land_"+name+"_rough", () -> LandBiome.createRoughBiome(precipitation, temperature, downfall));
		OCEAN = register.register("land_"+name+"_ocean", () -> LandBiome.createOceanBiome(precipitation, temperature, downfall));
	}
	
	@Override
	public List<Biome> getAll()
	{
		return ImmutableList.of(NORMAL.get(), ROUGH.get(), OCEAN.get());
	}
	
	@Override
	public Biome fromType(LandBiomeType type)
	{
		switch(type)
		{
			case NORMAL: default: return NORMAL.get();
			case ROUGH: return ROUGH.get();
			case OCEAN: return OCEAN.get();
		}
	}
	
	public LandBiomeType getTypeFromBiome(Biome biome)
	{
		if(biome == NORMAL.get())
			return LandBiomeType.NORMAL;
		else if(biome == ROUGH.get())
			return LandBiomeType.ROUGH;
		else if(biome == OCEAN.get())
			return LandBiomeType.OCEAN;
		else throw new IllegalArgumentException("Got biome \"" + biome + "\" which is not in the biome set: " + name);
	}
	
	public static LandBiomeSet getSet(ChunkGenerator generator)
	{
		if(generator instanceof LandChunkGenerator)
			return ((LandChunkGenerator) generator).landTypes.getTerrain().getBiomeSet();
		else return MSBiomes.DEFAULT_LAND;
	}
}