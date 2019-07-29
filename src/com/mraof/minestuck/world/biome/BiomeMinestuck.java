package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.Minestuck;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class BiomeMinestuck extends Biome
{
	
	public static final SkaiaBiome SKAIA = getNull();
	public static Biome mediumOcean, mediumNormal, mediumRough;
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull() {
		return null;
	}
	
	
	protected BiomeMinestuck(Builder biomeBuilder)
	{
		super(biomeBuilder);
	}
	
	@SubscribeEvent
	public static void registerBiomes(final RegistryEvent.Register<Biome> event)
	{
		event.getRegistry().register(new SkaiaBiome().setRegistryName("skaia"));
		/*mediumNormal = new BiomeMinestuck(new BiomeProperties("The Medium")).setRegistryName("medium");
		mediumOcean = new BiomeMinestuck(new BiomeProperties("The Medium (Ocean)").setBaseBiome("medium")).setRegistryName("medium_ocean");
		mediumRough = new BiomeMinestuck(new BiomeProperties("The Medium (Rough)").setBaseBiome("medium")).setRegistryName("medium_rough");
		event.getRegistry().register(mediumNormal);
		event.getRegistry().register(mediumOcean);
		event.getRegistry().register(mediumRough);*/
	}
	
	public static void init()
	{
		SKAIA.init();
	}
}
