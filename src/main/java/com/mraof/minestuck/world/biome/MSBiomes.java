package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MSBiomes
{
	public static final ResourceKey<Biome> SKAIA = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Minestuck.MOD_ID, "skaia"));
	
	public static final LandBiomeSetType DEFAULT_LAND = new LandBiomeSetType(Minestuck.MOD_ID, "default", Biome.Precipitation.RAIN, 0.7F, 0.5F);
	public static final LandBiomeSetType HIGH_HUMID_LAND = new LandBiomeSetType(Minestuck.MOD_ID, "high_humid", Biome.Precipitation.RAIN, 1.0F, 0.9F);
	public static final LandBiomeSetType NO_RAIN_LAND = new LandBiomeSetType(Minestuck.MOD_ID, "no_rain", Biome.Precipitation.NONE, 2.0F, 0.0F);
	public static final LandBiomeSetType SNOW_LAND = new LandBiomeSetType(Minestuck.MOD_ID, "snow", Biome.Precipitation.SNOW, 0.0F, 0.5F);
	
}