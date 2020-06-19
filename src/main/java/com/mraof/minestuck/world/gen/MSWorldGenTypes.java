package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.biome.gen.LandBiomeProvider;
import com.mraof.minestuck.world.biome.gen.LandBiomeProviderSettings;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MSWorldGenTypes
{
	public static final ChunkGeneratorType<SkaiaGenSettings, SkaiaChunkGenerator> SKAIA = getNull();
	public static final ChunkGeneratorType<ProspitGenSettings, ProspitChunkGenerator> PROSPIT = getNull();
	public static final ChunkGeneratorType<DerseGenSettings, DerseChunkGenerator> DERSE = getNull();
	public static final ChunkGeneratorType<LandGenSettings, LandChunkGenerator> LANDS = getNull();
	
	@ObjectHolder(Minestuck.MOD_ID+":lands")
	public static final BiomeProviderType<LandBiomeProviderSettings, LandBiomeProvider> LAND_BIOMES = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull() {
		return null;
	}
	
	@SubscribeEvent
	public static void registerChunkGenerators(RegistryEvent.Register<ChunkGeneratorType<?, ?>> event)
	{
		event.getRegistry().register(new ChunkGeneratorType<>(SkaiaChunkGenerator::new, false, SkaiaGenSettings::new).setRegistryName("skaia"));
		event.getRegistry().register(new ChunkGeneratorType<>(ProspitChunkGenerator::new, false, ProspitGenSettings::new).setRegistryName("prospit"));
		event.getRegistry().register(new ChunkGeneratorType<>(DerseChunkGenerator::new, false, DerseGenSettings::new).setRegistryName("derse"));
		event.getRegistry().register(new ChunkGeneratorType<>(LandChunkGenerator::new, false, LandGenSettings::new).setRegistryName("lands"));
	}
	
	@SubscribeEvent
	public static void registerBiomeProviders(RegistryEvent.Register<BiomeProviderType<?, ?>> event)
	{
		event.getRegistry().register(new BiomeProviderType<>(LandBiomeProvider::new, LandBiomeProviderSettings::new).setRegistryName("lands"));
	}
}