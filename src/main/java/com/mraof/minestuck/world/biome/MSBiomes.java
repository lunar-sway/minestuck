package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public final class MSBiomes
{
	public static final ResourceKey<Biome> SKAIA = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "skaia"));
	public static final ResourceKey<Biome> PROSPIT = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "prospit"));
	public static final ResourceKey<Biome> DERSE = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "derse"));
	public static final ResourceKey<Biome> VEIL = ResourceKey.create(Registries.BIOME, Minestuck.id("veil"));
	
	public static final LandBiomeSetType DEFAULT_LAND = new LandBiomeSetType(Minestuck.MOD_ID, "default", true, 0.7F, 0.5F);
	public static final LandBiomeSetType HIGH_HUMID_LAND = new LandBiomeSetType(Minestuck.MOD_ID, "high_humid", true, 1.0F, 0.9F);
	public static final LandBiomeSetType NO_RAIN_LAND = new LandBiomeSetType(Minestuck.MOD_ID, "no_rain", false, 2.0F, 0.0F);
	public static final LandBiomeSetType SNOW_LAND = new LandBiomeSetType(Minestuck.MOD_ID, "snow", true, 0.0F, 0.5F);
	
}
