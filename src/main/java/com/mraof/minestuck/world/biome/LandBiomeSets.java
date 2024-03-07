package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.Minestuck;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LandBiomeSets
{
	public static final LandBiomeSetType DEFAULT_LAND = new LandBiomeSetType(Minestuck.MOD_ID, "default", true, 0.7F, 0.5F);
	public static final LandBiomeSetType HIGH_HUMID_LAND = new LandBiomeSetType(Minestuck.MOD_ID, "high_humid", true, 1.0F, 0.9F);
	public static final LandBiomeSetType NO_RAIN_LAND = new LandBiomeSetType(Minestuck.MOD_ID, "no_rain", false, 2.0F, 0.0F);
	public static final LandBiomeSetType SNOW_LAND = new LandBiomeSetType(Minestuck.MOD_ID, "snow", true, 0.0F, 0.5F);
}