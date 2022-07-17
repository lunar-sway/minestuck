package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;
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
		if (event.getWorld() instanceof ServerLevel level)
		{
			if (level.getChunkSource().getGenerator() instanceof LandChunkGenerator generator)
			{
				LandProperties properties = LandProperties.create(generator.landTypes);
				
				if (level.levelData instanceof ServerLevelData levelData)
					level.levelData = new LandWorldInfo(levelData, properties.forceRain, properties.forceThunder, properties.skylightBase);
				else
					LOGGER.error("Expected level data on server side to be an instance of IServerWorldInfo. Was {}", level.levelData.getClass());
			}
		}
	}
}