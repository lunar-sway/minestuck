package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class WeatherManager
{
	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event)
	{
		//TODO this is a flawed hack to control weather for specific dimensions. Try to find alternatives.
		if (event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER && event.world instanceof ServerWorld)
		{
			ServerWorld world = (ServerWorld) event.world;
			if (world.getChunkSource().getGenerator() instanceof LandChunkGenerator)
			{
				LandChunkGenerator generator = (LandChunkGenerator) world.getChunkSource().getGenerator();
				if(generator.forceRain == LandProperties.ForceType.OFF)
					world.rainLevel = 0.0F;
				else if(generator.forceRain == LandProperties.ForceType.ON)
					world.rainLevel = 1.0F;
				
				if(generator.forceThunder == LandProperties.ForceType.OFF)
					world.thunderLevel = 0.0F;
				else if(generator.forceThunder == LandProperties.ForceType.ON)
					world.thunderLevel = 1.0F;
			}
		}
	}
}