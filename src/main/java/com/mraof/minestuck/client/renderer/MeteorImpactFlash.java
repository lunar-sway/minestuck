package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.MeteorClientHandler;
import com.mraof.minestuck.entry.meteor.MeteorManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class MeteorImpactFlash
{
	// 2.5 seconds at 20 tps
	private static final int FLASH_DURATION_TICKS = 50;
	
	@SubscribeEvent
	public static void onRenderGui(RenderGuiEvent.Post event)
	{
		if(!MinestuckConfig.CLIENT.meteorImpactFlash.get()) return;
		if(!MeteorClientHandler.hasActiveMeteor()) return;
		
		int ticksElapsed = MeteorClientHandler.getLocalPlayerMeteorTicks();
		int ticksLeft = MeteorManager.TOTAL_TICKS - ticksElapsed;
		
		if(ticksLeft > FLASH_DURATION_TICKS) return;
		
		float clampedTicksLeft = Math.max(0, ticksLeft);
		float progress = 1.0f - clampedTicksLeft / FLASH_DURATION_TICKS;
		float intensity = progress * progress;
		
		Minecraft mc = Minecraft.getInstance();
		GuiGraphics graphics = event.getGuiGraphics();
		
		int alpha = Math.round(intensity * 255.0f) << 24;
		int color = alpha | 0xFFFFFF;
		
		graphics.fill(0, 0, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight(), color);
	}
}