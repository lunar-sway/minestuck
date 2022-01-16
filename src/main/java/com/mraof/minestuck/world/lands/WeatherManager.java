package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class WeatherManager
{
	private static final Logger LOGGER = LogManager.getLogger();
	
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
				
				if (world.levelData instanceof IServerWorldInfo)
					world.levelData = new LandWorldInfo((IServerWorldInfo) world.levelData, properties.forceRain, properties.forceThunder);
				else
					LOGGER.error("Expected level data on server side to be an instance of IServerWorldInfo. Was {}", world.levelData.getClass());
			}
		}
	}
}