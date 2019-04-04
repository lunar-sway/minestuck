package com.mraof.minestuck.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MinestuckSoundHandler
{
	public static final MinestuckSoundHandler instance = new MinestuckSoundHandler();
	
	public static SoundEvent soundEmissaryOfDance;
	public static SoundEvent soundDanceStabDance;
	public static SoundEvent soundRetroBattleTheme;
	public static SoundEvent soundFrogAmbient;
	public static SoundEvent soundWarhorn;
	public static SoundEvent soundWhispers;
	public static SoundEvent soundUpcheladder;
	public static SoundEvent soundImpAmbient;
	public static SoundEvent soundOgreAmbient;
	public static SoundEvent soundOgreHurt;
	public static SoundEvent soundOgreDeath;
	public static SoundEvent soundLichAmbient;
	public static SoundEvent soundLichHurt;
	public static SoundEvent soundLichDeath;
	
	public static void initSound()
	{
		ResourceLocation soundLocation = new ResourceLocation("minestuck", "record.emissary");
		soundEmissaryOfDance = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "record.danceStab");
		soundDanceStabDance = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","record.retroBattle");
		soundRetroBattleTheme = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "warhorn");
		soundWarhorn = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "whispers");
		soundWhispers = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "upcheladder");
		soundUpcheladder = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		
		//Frogs
		soundLocation = new ResourceLocation("minestuck","mobs.frogAmbient");
		soundFrogAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Imps
		soundLocation = new ResourceLocation("minestuck", "impAmbient");
		soundImpAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Ogres
		soundLocation = new ResourceLocation("minestuck", "ogreAmbient");
		soundOgreAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "ogreHurt");
		soundOgreHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "ogreDeath");
		soundOgreDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Liches
		soundLocation = new ResourceLocation("minestuck", "lichAmbient");
		soundLichAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "lichHurt");
		soundLichHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "lichDeath");
		soundLichDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
	}
	
	@SubscribeEvent
	public void registerSound(RegistryEvent.Register<SoundEvent> event)
	{
		event.getRegistry().register(soundEmissaryOfDance);
		event.getRegistry().register(soundDanceStabDance);
		event.getRegistry().register(soundRetroBattleTheme);
		event.getRegistry().register(soundWarhorn);
		event.getRegistry().register(soundWhispers);
		event.getRegistry().register(soundUpcheladder);
		event.getRegistry().register(soundImpAmbient);
		event.getRegistry().register(soundOgreAmbient);
		event.getRegistry().register(soundOgreHurt);
		event.getRegistry().register(soundOgreDeath);
		event.getRegistry().register(soundLichAmbient);
		event.getRegistry().register(soundLichHurt);
		event.getRegistry().register(soundLichDeath);
		//event.getRegistry().register(soundFrogAmbient);
	}
}
