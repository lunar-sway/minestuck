package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.event.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, value = Dist.CLIENT)
public class MSMusicTicker    //TODO Introduce types (something similar to vanilla) such that this class could be reused for prospit, derse etc
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END && !Minecraft.getInstance().isPaused())
		{
			tick(Minecraft.getInstance());
		}
	}
	
	@SubscribeEvent
	public static void playSound(PlaySoundEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.level != null && ClientDimensionData.isLand(mc.level.dimension())
				&& event.getSound().getLocation().equals(mc.getSituationalMusic().getEvent().value().getLocation()))
			event.setSound(null);
	}
	
	private static boolean wasInLand = false;
	private static int ticksUntilMusic;
	private static SoundInstance currentMusic;
	
	private static void tick(Minecraft mc)
	{
		LandTypePair types = mc.level != null ? ClientDimensionData.getLandTypes(mc.level.dimension()) : null;
		if(types != null)
		{
			if(!wasInLand)
			{
				ticksUntilMusic = Mth.nextInt(mc.level.random, 0, 6000);
				LOGGER.debug("Entered a land. Land music scheduled to play in {} ticks", ticksUntilMusic);
			}
			
			if(currentMusic == null)
			{
				ticksUntilMusic--;
				if(ticksUntilMusic < 0)
				{
					currentMusic = SimpleSoundInstance.forMusic(getLandSoundEvent(mc.level.random, types));
					mc.getSoundManager().play(currentMusic);
					LOGGER.debug("Land music started.");
				}
			} else if(!mc.getSoundManager().isActive(currentMusic))
			{
				currentMusic = null;
				ticksUntilMusic = Mth.nextInt(mc.level.random, 12000, 24000);
				LOGGER.debug("Land music finished playing. Scheduling music to be played again in {} ticks.", ticksUntilMusic);
			}
			
			wasInLand = true;
		} else
		{
			wasInLand = false;
			if(currentMusic != null)
			{
				mc.getSoundManager().stop(currentMusic);
				currentMusic = null;
				LOGGER.debug("Left land, stopped music.");
			}
		}
	}
	
	private static SoundEvent getLandSoundEvent(RandomSource rand, LandTypePair pair)
	{
		if(rand.nextInt(5) == 0)
		{
			return MSSoundEvents.MUSIC_UNIVERSAL.get();
		}
		
		if(rand.nextBoolean())
			return pair.getTerrain().getBackgroundMusic();
		else return pair.getTitle().getBackgroundMusic();
	}
}