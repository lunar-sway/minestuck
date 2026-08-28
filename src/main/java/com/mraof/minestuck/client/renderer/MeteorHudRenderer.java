package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entry.meteor.MeteorManager;
import net.minecraft.client.Minecraft;
import com.mraof.minestuck.client.MeteorClientHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class MeteorHudRenderer
{
	private static final String METEOR_SYMBOL = "\u2604";
	
	@SubscribeEvent
	public static void onRenderGui(RenderGuiEvent.Post event)
	{
		if(!MeteorClientHandler.hasActiveMeteor()) return;
		
		int ticksElapsed = MeteorClientHandler.getLocalPlayerMeteorTicks();
		int totalTicks = MeteorManager.TOTAL_TICKS;
		int ticksLeft = totalTicks - ticksElapsed;
		int secondsLeft = ticksLeft / 20;
		int minutes = secondsLeft / 60;
		int seconds = secondsLeft % 60;
		
		String timeText = String.format("%s %d:%02d", METEOR_SYMBOL, minutes, seconds);
		
		Minecraft mc = Minecraft.getInstance();
		GuiGraphics graphics = event.getGuiGraphics();
		int screenWidth = mc.getWindow().getGuiScaledWidth();
		
		boolean lastMinute = secondsLeft <= 60;
		boolean blink = lastMinute && (ticksLeft / 10) % 2 == 0;
		int color = blink ? 0xFFFF0000 : (lastMinute ? 0xFFFF4444 : 0xFFFFFFFF);
		
		int x = screenWidth / 2 - mc.font.width(timeText) / 2;
		int y = 10;
		
		graphics.drawString(mc.font, timeText, x + 1, y + 1, 0xFF000000, false);
		graphics.drawString(mc.font, timeText, x, y, color, false);
	}
	
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event)
	{
		MeteorClientHandler.clientTick();
	}
}