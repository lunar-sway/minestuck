package com.mraof.minestuck.world.biome;

import com.google.common.collect.ImmutableSet;
import com.mraof.minestuck.world.gen.LandGenSettings;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.Set;

public class LandBiomeSet
{
	public final RegistryObject<LandBiome> NORMAL, ROUGH, OCEAN;
	
	public LandBiomeSet(DeferredRegister<Biome> register, String name, Biome.RainType precipitation)
	{
		NORMAL = register.register("land_"+name+"_normal", () -> new LandBiome.Normal(precipitation));
		ROUGH = register.register("land_"+name+"_rough", () -> new LandBiome.Rough(precipitation));
		OCEAN = register.register("land_"+name+"_ocean", () -> new LandBiome.Ocean(precipitation));
	}
	
	void init()
	{
		NORMAL.get().init();
		ROUGH.get().init();
		OCEAN.get().init();
	}
	
	public Set<Biome> getAll()
	{
		return ImmutableSet.of(NORMAL.get(), ROUGH.get(), OCEAN.get());
	}
	
	public LandBiome fromType(BiomeType type)
	{
		switch(type)
		{
			case NORMAL: default: return NORMAL.get();
			case ROUGH: return ROUGH.get();
			case OCEAN: return OCEAN.get();
		}
	}
	
	public static LandBiomeSet getSet(GenerationSettings settings)
	{
		if(settings instanceof LandGenSettings)
			return ((LandGenSettings) settings).getLandTypes().terrain.getBiomeSet();
		else return MSBiomes.DEFAULT_LAND;
	}
}