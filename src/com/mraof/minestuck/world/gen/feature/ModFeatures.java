package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import net.minecraft.world.gen.feature.BushConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures
{
	public static final Feature<NoFeatureConfig> RAINBOW_TREE = getNull();
	public static final Feature<NoFeatureConfig> END_TREE =	 getNull();
	public static final Feature<NoFeatureConfig> FIRE_FIELD = getNull();
	public static final Feature<BushConfig> PILLAR = getNull();
	public static final Feature<BushConfig> LARGE_PILLAR = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	@SuppressWarnings("unused")
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
	{
		IForgeRegistry<Feature<?>> registry = event.getRegistry();
		
		registry.register(new RainbowTreeFeature(NoFeatureConfig::deserialize, false).setRegistryName("rainbow_tree"));
		registry.register(new EndTreeFeature(NoFeatureConfig::deserialize, false).setRegistryName("end_tree"));
		registry.register(new FireFieldFeature(NoFeatureConfig::deserialize).setRegistryName("fire_field"));
		registry.register(new PillarFeature(BushConfig::deserialize, false).setRegistryName("pillar"));
		registry.register(new PillarFeature(BushConfig::deserialize, true).setRegistryName("large_pillar"));
	}
}