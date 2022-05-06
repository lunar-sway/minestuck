package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.Minestuck;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MSBiomes
{
	public static final RegistryKey<Biome> SKAIA = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Minestuck.MOD_ID, "skaia"));
	
	public static final LandBiomeSet DEFAULT_LAND = new LandBiomeSet(Minestuck.MOD_ID, "default", Biome.RainType.RAIN, 0.7F, 0.5F);
	public static final LandBiomeSet HIGH_HUMID_LAND = new LandBiomeSet(Minestuck.MOD_ID, "high_humid", Biome.RainType.RAIN, 1.0F, 0.9F);
	public static final LandBiomeSet NO_RAIN_LAND = new LandBiomeSet(Minestuck.MOD_ID, "no_rain", Biome.RainType.NONE, 2.0F, 0.0F);
	public static final LandBiomeSet SNOW_LAND = new LandBiomeSet(Minestuck.MOD_ID, "snow", Biome.RainType.SNOW, 0.0F, 0.5F);
	
}