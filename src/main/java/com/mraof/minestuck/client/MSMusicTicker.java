package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(modid = Minestuck.MOD_ID, value = Dist.CLIENT)
public class MSMusicTicker    //TODO Introduce types (something similar to vanilla) such that this class could be reused for prospit, derse etc
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@SubscribeEvent
	public static void clientTick(ClientTickEvent.Post event)
	{
		if(!Minecraft.getInstance().isPaused())
		{
			tick(Minecraft.getInstance());
		}
	}
	
	@SubscribeEvent
	public static void playSound(PlaySoundEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.level != null && ClientDimensionData.isInMedium(mc.level.dimension())
				&& event.getSound().getLocation().equals(mc.getSituationalMusic().getEvent().value().getLocation()))
			event.setSound(null);
	}
	
	private static boolean wasInMedium = false;
	private static int ticksUntilMusic;
	private static SoundInstance currentMusic;
	
	private static void tick(Minecraft mc)
	{
		if(mc.level != null && ClientDimensionData.isInMedium(mc.level.dimension()))
		{
			handleMusic(mc);
		} else
		{
			wasInMedium = false;
			if(currentMusic != null)
			{
				mc.getSoundManager().stop(currentMusic);
				currentMusic = null;
				LOGGER.debug("Left Medium, stopped music.");
			}
		}
	}
	
	private static void handleMusic(Minecraft mc)
	{
		ClientLevel level = mc.level;
		
		if(!wasInMedium)
		{
			ticksUntilMusic = Mth.nextInt(level.random, 0, 20);
			LOGGER.debug("Entered the Medium. MS music scheduled to play in {} ticks", ticksUntilMusic);
		}
		
		if(currentMusic == null)
		{
			ticksUntilMusic--;
			if(ticksUntilMusic < 0)
			{
				SoundEvent newMusic = getDimensionSoundEvent(level);
				if(newMusic != null)
				{
					currentMusic = SimpleSoundInstance.forMusic(newMusic);
					mc.getSoundManager().play(currentMusic);
					LOGGER.debug("MS music started.");
				} else
				{
					LOGGER.debug("Failed to start MS music");
				}
			}
		} else if(!mc.getSoundManager().isActive(currentMusic))
		{
			currentMusic = null;
			ticksUntilMusic = Mth.nextInt(level.random, 100, 100);
			LOGGER.debug("MS music finished playing. Scheduling music to be played again in {} ticks.", ticksUntilMusic);
		}
		
		wasInMedium = true;
	}
	
	private static SoundEvent getDimensionSoundEvent(ClientLevel level)
	{
		ResourceKey<Level> dimension = level.dimension();
		LandTypePair pair = ClientDimensionData.getLandTypes(dimension);
		
		if(pair != null)
		{
			RandomSource rand = level.random;
			
			if(rand.nextInt(5) == 0)
				return MSSoundEvents.MUSIC_UNIVERSAL.get();
			
			if(rand.nextBoolean())
				return pair.getTerrain().getBackgroundMusic();
			else return pair.getTitle().getBackgroundMusic();
		} else if(MSDimensions.isProspit(dimension))
		{
			return MSSoundEvents.MUSIC_PROSPIT.get();
		} else if(MSDimensions.isDerse(dimension))
		{
			return MSSoundEvents.MUSIC_DERSE.get();
		} else if(MSDimensions.isVeil(dimension))
		{
			return MSSoundEvents.MUSIC_VEIL.get();
		} else if(MSDimensions.isSkaia(dimension))
		{
			return MSSoundEvents.MUSIC_SKAIA.get();
		}
		
		return null;
	}
}