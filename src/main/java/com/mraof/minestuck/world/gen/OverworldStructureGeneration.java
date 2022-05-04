package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class OverworldStructureGeneration
{
	private static final StructureSeparationSettings FROG_TEMPLE_SETTINGS = new StructureSeparationSettings(140, 92, 41361201);
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onWorldLoad(WorldEvent.Load event)
	{
		if (event.getWorld() instanceof ServerWorld)
		{
			ServerWorld world = (ServerWorld) event.getWorld();
			if (world.dimension() == World.OVERWORLD)
			{
				try
				{
					world.getChunkSource().getGenerator().getSettings().structureConfig().put(MSFeatures.FROG_TEMPLE, FROG_TEMPLE_SETTINGS);
				} catch(UnsupportedOperationException ignored)
				{}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onBiomeLoad(BiomeLoadingEvent event)
	{
		ResourceLocation name = event.getName();
		if (name != null && BiomeDictionary.hasType(RegistryKey.create(Registry.BIOME_REGISTRY, name), BiomeDictionary.Type.OVERWORLD))
		{
			event.getGeneration().addStructureStart(MSFeatures.FROG_TEMPLE.configured(IFeatureConfig.NONE));
		}
	}
}