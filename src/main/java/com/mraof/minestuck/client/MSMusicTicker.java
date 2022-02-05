package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

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
				&& event.getSound().getLocation().equals(mc.getSituationalMusic().getEvent().getLocation()))
			event.setResultSound(null);
	}
	
	private static boolean wasInLand = false;
	private static int ticksUntilMusic;
	private static ISound currentMusic;
	
	private static void tick(Minecraft mc)
	{
		LandTypePair types = mc.level != null ? ClientDimensionData.getLandTypes(mc.level.dimension()) : null;
		if(types != null)
		{
			if(!wasInLand)
			{
				ticksUntilMusic = MathHelper.nextInt(mc.level.random, 0, 6000);
				LOGGER.debug("Entered a land. Land music scheduled to play in {} ticks", ticksUntilMusic);
			}
			
			if(currentMusic == null)
			{
				ticksUntilMusic--;
				if(ticksUntilMusic < 0)
				{
					if(mc.level.random.nextInt(5) == 0)
					{
						currentMusic = SimpleSound.forMusic(getGenericSoundEvent(mc.level.random));
					} else
						currentMusic = SimpleSound.forMusic(getLandSoundEvent(mc.level.random, types));
					mc.getSoundManager().play(currentMusic);
					LOGGER.debug("Land music started.");
				}
			} else if(!mc.getSoundManager().isActive(currentMusic))
			{
				currentMusic = null;
				ticksUntilMusic = MathHelper.nextInt(mc.level.random, 12000, 24000);
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
	
	private static SoundEvent getLandSoundEvent(Random rand, LandTypePair pair)
	{
		if(rand.nextBoolean())
			return pair.getTerrain().getBackgroundMusic();
		else return pair.getTitle().getBackgroundMusic();
	}
	
	private static SoundEvent getGenericSoundEvent(Random rand)
	{
		int randomPick = rand.nextInt(3);
		if(randomPick == 2)
			return MSSoundEvents.MUSIC_SICKEST_FIRES;
		else if(randomPick == 1)
			return MSSoundEvents.MUSIC_RANCHOROUS_GAMBLIGANT;
		else
			return MSSoundEvents.MUSIC_WHAT_GOES_UP;
	}
}