package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.Minestuck;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MSBiomes
{
	public static final DeferredRegister<Biome> REGISTER = DeferredRegister.create(ForgeRegistries.BIOMES, Minestuck.MOD_ID);
	
	public static final RegistryObject<Biome> SKAIA = null;//REGISTER.register("skaia", SkaiaBiome::new);
	public static final LandBiomeSet DEFAULT_LAND = new LandBiomeSet(REGISTER, "default", Biome.RainType.RAIN, 0.7F, 0.5F);
	public static final LandBiomeSet HIGH_HUMID_LAND = new LandBiomeSet(REGISTER, "high_humid", Biome.RainType.RAIN, 1.0F, 0.9F);
	public static final LandBiomeSet NO_RAIN_LAND = new LandBiomeSet(REGISTER, "no_rain", Biome.RainType.NONE, 2.0F, 0.0F);
	public static final LandBiomeSet SNOW_LAND = new LandBiomeSet(REGISTER, "snow", Biome.RainType.SNOW, 0.0F, 0.5F);
	
	@SubscribeEvent
	public static void onMissingMappings(RegistryEvent.MissingMappings<Biome> event)
	{
		event.getAllMappings().stream().filter(mapping -> mapping.key.equals(new ResourceLocation("minestuck:land_normal"))).forEach(mapping -> mapping.remap(DEFAULT_LAND.NORMAL.get()));
		event.getAllMappings().stream().filter(mapping -> mapping.key.equals(new ResourceLocation("minestuck:land_rough"))).forEach(mapping -> mapping.remap(DEFAULT_LAND.ROUGH.get()));
		event.getAllMappings().stream().filter(mapping -> mapping.key.equals(new ResourceLocation("minestuck:land_ocean"))).forEach(mapping -> mapping.remap(DEFAULT_LAND.OCEAN.get()));
	}
}