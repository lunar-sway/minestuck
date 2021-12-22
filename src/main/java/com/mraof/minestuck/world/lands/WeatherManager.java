package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class WeatherManager
{
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event)
	{
		if (event.getWorld() instanceof ServerWorld)
		{
			ServerWorld world = (ServerWorld) event.getWorld();
			if (world.getChunkSource().getGenerator() instanceof LandChunkGenerator)
			{
				LandChunkGenerator generator = (LandChunkGenerator) world.getChunkSource().getGenerator();
				LandProperties properties = LandProperties.create(generator.landTypes);
				if (properties.forceRain != LandProperties.ForceType.DEFAULT || properties.forceThunder != LandProperties.ForceType.DEFAULT)
				{
					world.levelData = new ForcedWeatherWorldInfo(world.levelData, properties.forceRain, properties.forceThunder);
				}
			}
		}
	}
}