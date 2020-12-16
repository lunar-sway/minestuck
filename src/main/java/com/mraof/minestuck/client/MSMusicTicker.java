package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.MSDimensions;
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

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, value = Dist.CLIENT)
public class MSMusicTicker	//TODO Introduce types (something similar to vanilla) such that this class could be reused for prospit, derse etc
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END && !Minecraft.getInstance().isGamePaused())
		{
			tick(Minecraft.getInstance());
		}
	}
	
	@SubscribeEvent
	public static void playSound(PlaySoundEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.world != null && MSDimensions.isLandDimension(mc.world.getDimensionKey())
				&& event.getSound().getSoundLocation().equals(mc.getBackgroundMusicSelector().getSoundEvent().getName()))
			event.setResultSound(null);
	}
	
	private static boolean wasInLand = false;
	private static int ticksUntilMusic;
	private static ISound currentMusic;
	
	private static void tick(Minecraft mc)
	{
		if(mc.world != null && MSDimensions.isLandDimension(mc.world.getDimensionKey()))
		{
			if(!wasInLand)
			{
				ticksUntilMusic = MathHelper.nextInt(mc.world.rand, 0, 6000);
				LOGGER.debug("Entered a land. Land music scheduled to play in {} ticks", ticksUntilMusic);
			}
			
			if(currentMusic == null)
			{
				ticksUntilMusic--;
				if(ticksUntilMusic < 0)
				{
					currentMusic = SimpleSound.music(getLandSoundEvent(mc));
					mc.getSoundHandler().play(currentMusic);
					LOGGER.debug("Land music started.");
				}
			} else if(!mc.getSoundHandler().isPlaying(currentMusic))
			{
				currentMusic = null;
				ticksUntilMusic = MathHelper.nextInt(mc.world.rand, 12000, 24000);
				LOGGER.debug("Land music finished playing. Scheduling music to be played again in {} ticks.", ticksUntilMusic);
			}
			
			wasInLand = true;
		} else
		{
			wasInLand = false;
			if(currentMusic != null)
			{
				mc.getSoundHandler().stop(currentMusic);
				currentMusic = null;
				LOGGER.debug("Left land, stopped music.");
			}
		}
	}
	
	private static SoundEvent getLandSoundEvent(Minecraft mc)
	{
		//LandDimension dim = (LandDimension) mc.world.getDimension(); TODO
		
		LandTypePair pair = null;//dim.landTypes;
		
		if(mc.world.rand.nextBoolean())
			return pair.terrain.getBackgroundMusic();
		else return pair.title.getBackgroundMusic();
	}
}