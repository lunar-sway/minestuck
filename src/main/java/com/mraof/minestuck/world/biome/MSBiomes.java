package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.Minestuck;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MSBiomes
{
	public static final DeferredRegister<Biome> REGISTER = DeferredRegister.create(ForgeRegistries.BIOMES, Minestuck.MOD_ID);
	
	public static final RegistryObject<SkaiaBiome> SKAIA = REGISTER.register("skaia", SkaiaBiome::new);
	public static final LandBiomeSet DEFAULT_LAND = new LandBiomeSet(REGISTER, "default", Biome.RainType.RAIN);
	public static final LandBiomeSet NO_RAIN_LAND = new LandBiomeSet(REGISTER, "no_rain", Biome.RainType.NONE);
	public static final LandBiomeSet SNOW_LAND = new LandBiomeSet(REGISTER, "snow", Biome.RainType.SNOW);
	
	public static void init()
	{
		SKAIA.get().init();
		DEFAULT_LAND.init();
		NO_RAIN_LAND.init();
		SNOW_LAND.init();
	}
}