package com.mraof.minestuck.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BiomeMinestuck extends Biome
{
	
	public static Biome mediumOcean, mediumNormal, mediumRough;
	public BiomeMinestuck(BiomeProperties properties)
	{
		super(properties);
	}
	
	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event)
	{
		mediumNormal = new BiomeMinestuck(new BiomeProperties("The Medium")).setRegistryName("medium");
		mediumOcean = new BiomeMinestuck(new BiomeProperties("The Medium (Ocean)").setBaseBiome("medium")).setRegistryName("medium_ocean");
		mediumRough = new BiomeMinestuck(new BiomeProperties("The Medium (Rough)").setBaseBiome("medium")).setRegistryName("medium_rough");
		event.getRegistry().register(mediumNormal);
		event.getRegistry().register(mediumOcean);
		event.getRegistry().register(mediumRough);
	}
}
