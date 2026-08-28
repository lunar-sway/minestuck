package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MeteorPackets;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
/**
 * Clientside handler for meteor events.
 * Manages sound playback and provides data for the renderer.
 */
@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class MeteorClientHandler
{
	
	// entityId -> last known ticks elapsed
	private static final Map<Integer, Integer> activeMeteorTicks = new HashMap<>();
	@Nullable
	private static String localPlayerMeteorKey = null;
	private static int localPlayerMeteorTicks = 0;
	@Nullable
	private static net.minecraft.client.resources.sounds.SoundInstance currentMusicInstance = null;
	
	public static void onMusicTrigger(boolean play)
	{
		Minecraft mc = Minecraft.getInstance();
		
		if(play)
		{
			mc.getMusicManager().stopPlaying();
			mc.getSoundManager().stop(currentMusicInstance);
			
			SimpleSoundInstance sound = SimpleSoundInstance.forMusic(MSSoundEvents.METEOR_THEME.get());
			mc.getSoundManager().play(sound);
			currentMusicInstance = sound;
		} else
		{
			if(currentMusicInstance != null)
			{
				mc.getSoundManager().stop(currentMusicInstance);
				currentMusicInstance = null;
			}
			mc.getMusicManager().stopPlaying();
		}
	}
	
	private static long countdownStartGameTime = -1;
	
	public static void clientTick()
	{
		if(localPlayerMeteorKey == null) return;
		if(Minecraft.getInstance().level == null) return;
		
		long currentTime = Minecraft.getInstance().level.getGameTime();
		localPlayerMeteorTicks = (int) (currentTime - countdownStartGameTime);
		
		if(currentMusicInstance != null && Minecraft.getInstance().getSoundManager().isActive(currentMusicInstance))
		{
			Minecraft.getInstance().getMusicManager().stopPlaying();
		}
	}
	
	public static void onCountdownStart(MeteorPackets.CountdownStart packet)
	{
		activeMeteorTicks.put(packet.meteorEntityId(), packet.ticksElapsed());
		localPlayerMeteorKey = packet.playerKey();
		localPlayerMeteorTicks = packet.ticksElapsed();
		
		if(Minecraft.getInstance().level != null)
			countdownStartGameTime = Minecraft.getInstance().level.getGameTime() - packet.ticksElapsed();
	}
	
	
	public static void onMeteorPosition(MeteorPackets.MeteorPosition packet)
	{
		activeMeteorTicks.put(packet.entityId(), packet.ticksElapsed());
		localPlayerMeteorTicks = packet.ticksElapsed();
	}
	
	public static boolean hasActiveMeteor()
	{
		return localPlayerMeteorKey != null;
	}
	
	public static int getLocalPlayerMeteorTicks()
	{
		return localPlayerMeteorTicks;
	}
	
	public static int getLocalPlayerMeteorEntityId()
	{
		return activeMeteorTicks.keySet().stream().findFirst().orElse(-1);
	}
	
	public static void onMeteorRemoved(int entityId)
	{
		activeMeteorTicks.remove(entityId);
		if(activeMeteorTicks.isEmpty())
		{
			localPlayerMeteorKey = null;
			localPlayerMeteorTicks = 0;
			countdownStartGameTime = -1;
		}
	}
	
	public static int getTicksElapsedForMeteor(int entityId)
	{
		return activeMeteorTicks.getOrDefault(entityId, 0);
	}
	
	public static boolean hasMeteor(int entityId)
	{
		return activeMeteorTicks.containsKey(entityId);
	}
	
	@SubscribeEvent
	public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event)
	{
		activeMeteorTicks.clear();
		localPlayerMeteorKey = null;
		localPlayerMeteorTicks = 0;
		countdownStartGameTime = -1;
		
		if(currentMusicInstance != null)
		{
			Minecraft.getInstance().getSoundManager().stop(currentMusicInstance);
			currentMusicInstance = null;
		}
	}
}