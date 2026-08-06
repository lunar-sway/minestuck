package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.MeteorClientHandler;
import com.mraof.minestuck.entity.MeteorEntity;
import com.mraof.minestuck.entry.meteor.MeteorManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class MeteorSkyRenderer
{
	private static final int FRAME_COUNT = 3;
	private static final int TICKS_PER_FRAME = 2;
	private static final float MIN_SIZE = 1.5F;
	private static final float MAX_SIZE = 3.0F;
	
	@SubscribeEvent
	public static void meteorInSky(RenderLevelStageEvent event)
	{
		if(!MeteorClientHandler.hasActiveMeteor())
			return;
		
		if(event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY)
			return;
		
		Minecraft mc = Minecraft.getInstance();
		ClientLevel level = mc.level;
		if(level == null)
			return;
		
		int entityId = MeteorClientHandler.getLocalPlayerMeteorEntityId();
		boolean dashPhase;
		if(entityId != -1 && level.getEntity(entityId) instanceof MeteorEntity meteor)
			dashPhase = meteor.isDashPhase();
		else
			dashPhase = MeteorClientHandler.getLocalPlayerMeteorTicks() >= MeteorManager.DASH_PHASE_TICKS;
		
		if(dashPhase)
			return;
		
		int frameIndex = (int) ((level.getGameTime() / TICKS_PER_FRAME) % FRAME_COUNT);
		
		float progress = (MeteorClientHandler.getLocalPlayerMeteorTicks() + event.getPartialTick().getGameTimeDeltaTicks()) / (float) MeteorManager.DASH_PHASE_TICKS;
		progress = Mth.clamp(progress, 0.0F, 1.0F);
		float size = Mth.lerp(progress * progress, MIN_SIZE, MAX_SIZE);
		
		Matrix4f matrix = new Matrix4f(event.getModelViewMatrix());
		SessionRenderHelper.drawSkyMeteor(matrix, size, LandSkySpriteUploader.getInstance().getSkyMeteorSprite(), frameIndex, FRAME_COUNT);
	}
}