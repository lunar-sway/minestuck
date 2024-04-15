package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.Minestuck;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class WeatherManager
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@SubscribeEvent
	public static void onWorldLoad(LevelEvent.Load event)
	{
		if(event.getLevel() instanceof ServerLevel level)
		{
			LandTypePair.getTypes(level).ifPresent(landTypes -> {
				LandProperties properties = LandProperties.create(landTypes);
				
				if(level.levelData instanceof ServerLevelData levelData)
					level.levelData = new LandWorldInfo(levelData, properties.forceRain, properties.forceThunder, properties.skylightBase);
				else
					LOGGER.error("Expected level data on server side to be an instance of IServerWorldInfo. Was {}", level.levelData.getClass());
			});
		}
	}
}