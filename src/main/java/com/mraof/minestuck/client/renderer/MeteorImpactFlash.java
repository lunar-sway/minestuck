package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.MeteorClientHandler;
import com.mraof.minestuck.entry.meteor.MeteorManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class MeteorImpactFlash
{
	private static final int FLASH_DURATION_TICKS = 50;
	
	@SubscribeEvent
	public static void onRenderGui(RenderGuiEvent.Post event)
	{
		if(!MinestuckConfig.CLIENT.meteorImpactFlash.get()) return;
		if(!MeteorClientHandler.hasActiveMeteor()) return;
		
		Minecraft mc = Minecraft.getInstance();
		if(mc.player == null || mc.level == null) return;
		
		BlockPos cruxtruderPos = MeteorClientHandler.getLocalCruxtruderPos();
		ResourceKey<Level> levelKey = MeteorClientHandler.getLocalLevelKey();
		if(cruxtruderPos == null || levelKey == null) return;
		
		if(mc.level.dimension() != levelKey) return;
		
		double maxDistance = MeteorManager.METEOR_CHUNK_RADIUS * 16.0;
		double distSq = mc.player.distanceToSqr(cruxtruderPos.getX() + 0.5, cruxtruderPos.getY(), cruxtruderPos.getZ() + 0.5);
		if(distSq > maxDistance * maxDistance) return;
		
		int ticksElapsed = MeteorClientHandler.getLocalPlayerMeteorTicks();
		int ticksLeft = MeteorManager.TOTAL_TICKS - ticksElapsed;
		
		if(ticksLeft > FLASH_DURATION_TICKS) return;
		
		float clampedTicksLeft = Math.max(0, ticksLeft);
		float progress = 1.0f - clampedTicksLeft / FLASH_DURATION_TICKS;
		float intensity = progress * progress;
		
		GuiGraphics graphics = event.getGuiGraphics();
		
		int alpha = Math.round(intensity * 255.0f) << 24;
		int color = alpha | 0xFFFFFF;
		
		graphics.fill(0, 0, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight(), color);
	}
}