package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.MeteorClientHandler;
import com.mraof.minestuck.entry.meteor.MeteorManager;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class MeteorSkyRenderer
{
	private static final int FRAME_COUNT = 3;
	private static final int TICKS_PER_FRAME = 2;
	
	@SubscribeEvent
	public static void meteorInSky(RenderLevelStageEvent event)
	{
		if(!MeteorClientHandler.hasActiveMeteor())
			return;
		
		if(event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY)
			return;
		
		if(MeteorClientHandler.getLocalPlayerMeteorTicks() >= MeteorManager.DASH_PHASE_TICKS)
			return;
		
		Minecraft mc = Minecraft.getInstance();
		if(mc.level == null)
			return;
		
		int frameIndex = (int) ((mc.level.getGameTime() / TICKS_PER_FRAME) % FRAME_COUNT);
		
		SessionRenderHelper.drawSkyMeteor(event.getModelViewMatrix(), 5, LandSkySpriteUploader.getInstance().getSkyMeteorSprite(), frameIndex, FRAME_COUNT);
	}
}