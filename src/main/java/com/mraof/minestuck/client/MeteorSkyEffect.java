package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entry.meteor.MeteorManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class MeteorSkyEffect
{
	@SubscribeEvent
	public static void onFogColor(ViewportEvent.ComputeFogColor event)
	{
		if(!MinestuckConfig.CLIENT.meteorSkyFog.get()) return;
		if(!MeteorClientHandler.hasActiveMeteor()) return;
		
		int ticksElapsed = MeteorClientHandler.getLocalPlayerMeteorTicks();
		int totalTicks = MeteorManager.TOTAL_TICKS;
		
		float progress = (float) ticksElapsed / totalTicks;
		
		float redIntensity = Math.max(0, (progress - 0.5f) * 2.0f);
		
		if(redIntensity <= 0) return;
		
		float currentR = event.getRed();
		float currentG = event.getGreen();
		float currentB = event.getBlue();
		
		event.setRed(Math.min(1.0f, currentR + redIntensity * 0.6f));
		event.setGreen(Math.max(0.0f, currentG - redIntensity * 0.3f));
		event.setBlue(Math.max(0.0f, currentB - redIntensity * 0.4f));
	}
}