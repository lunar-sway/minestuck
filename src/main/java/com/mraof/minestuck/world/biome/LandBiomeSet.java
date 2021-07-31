package com.mraof.minestuck.world.biome;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.List;

public class LandBiomeSet implements ILandBiomeSet
{
	public final RegistryObject<Biome> NORMAL, ROUGH, OCEAN;
	
	public LandBiomeSet(DeferredRegister<Biome> register, String name, Biome.RainType precipitation, float temperature, float downfall)
	{
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
	
	public static LandBiomeSet getSet(ChunkGenerator generator)
	{
		/*if(settings instanceof LandGenSettings)	TODO
			return ((LandGenSettings) settings).getLandTypes().terrain.getBiomeSet();
		else*/ return MSBiomes.DEFAULT_LAND;
	}
}