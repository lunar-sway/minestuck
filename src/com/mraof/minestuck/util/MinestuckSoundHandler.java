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
	public static SoundEvent soundWarhorn;
	public static SoundEvent soundWhispers;
	public static SoundEvent soundScreech;
	public static SoundEvent soundUpcheladder;
	public static SoundEvent soundNakagatorAmbient;
	public static SoundEvent soundNakagatorHurt;
	public static SoundEvent soundNakagatorDeath;
	public static SoundEvent soundSalamanderAmbient;
	public static SoundEvent soundSalamanderHurt;
	public static SoundEvent soundSalamanderDeath;
	public static SoundEvent soundIguanaAmbient;
	public static SoundEvent soundIguanaHurt;
	public static SoundEvent soundIguanaDeath;
	public static SoundEvent soundTurtleHurt;
	public static SoundEvent soundTurtleDeath;
	public static SoundEvent soundFrogAmbient;
	public static SoundEvent soundFrogHurt;
	public static SoundEvent soundFrogDeath;
	public static SoundEvent soundImpAmbient;
	public static SoundEvent soundImpHurt;
	public static SoundEvent soundImpDeath;
	public static SoundEvent soundOgreAmbient;
	public static SoundEvent soundOgreHurt;
	public static SoundEvent soundOgreDeath;
	public static SoundEvent soundBasiliskAmbient;
	public static SoundEvent soundBasiliskHurt;
	public static SoundEvent soundBasiliskDeath;
	public static SoundEvent soundLichAmbient;
	public static SoundEvent soundLichHurt;
	public static SoundEvent soundLichDeath;
	public static SoundEvent soundGiclopsAmbient;
	public static SoundEvent soundGiclopsHurt;
	public static SoundEvent soundGiclopsDeath;
	
	public static void initSound()
	{
		//Records
		ResourceLocation soundLocation = new ResourceLocation("minestuck", "record.emissary");
		soundEmissaryOfDance = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "record.danceStab");
		soundDanceStabDance = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","record.retroBattle");
		soundRetroBattleTheme = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		
		//Misc.
		soundLocation = new ResourceLocation("minestuck", "warhorn");
		soundWarhorn = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "whispers");
		soundWhispers = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "screech");
		soundScreech = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "upcheladder");
		soundUpcheladder = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		
		//Nakagators
		soundLocation = new ResourceLocation("minestuck","nakagatorAmbient");
		soundNakagatorAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","nakagatorHurt");
		soundNakagatorHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","nakagatorDeath");
		soundNakagatorDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Iguanas
		soundLocation = new ResourceLocation("minestuck","iguanaAmbient");
		soundIguanaAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","iguanaHurt");
		soundIguanaHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","iguanaDeath");
		soundIguanaDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Salamanders
		soundLocation = new ResourceLocation("minestuck","salamanderAmbient");
		soundSalamanderAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","salamanderHurt");
		soundSalamanderHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","salamanderDeath");
		soundSalamanderDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Turtles
		soundLocation = new ResourceLocation("minestuck","turtleHurt");
		soundTurtleHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","turtleDeath");
		soundTurtleDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		
		//Frogs
		soundLocation = new ResourceLocation("minestuck","frogAmbient");
		soundFrogAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","frogHurt");
		soundFrogHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","frogDeath");
		soundFrogDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		
		//Imps
		soundLocation = new ResourceLocation("minestuck", "impAmbient");
		soundImpAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "impHurt");
		soundImpHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "impDeath");
		soundImpDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Ogres
		soundLocation = new ResourceLocation("minestuck", "ogreAmbient");
		soundOgreAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "ogreHurt");
		soundOgreHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "ogreDeath");
		soundOgreDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Basilisks
		soundLocation = new ResourceLocation("minestuck", "basiliskAmbient");
		soundBasiliskAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "basiliskHurt");
		soundBasiliskHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "basiliskDeath");
		soundBasiliskDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Liches
		soundLocation = new ResourceLocation("minestuck", "lichAmbient");
		soundLichAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "lichHurt");
		soundLichHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "lichDeath");
		soundLichDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Giclops
		soundLocation = new ResourceLocation("minestuck", "giclopsAmbient");
		soundGiclopsAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "giclopsHurt");
		soundGiclopsHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "giclopsDeath");
		soundGiclopsDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
	}
	
	@SubscribeEvent
	public void registerSound(RegistryEvent.Register<SoundEvent> event)
	{
		event.getRegistry().register(soundEmissaryOfDance);
		event.getRegistry().register(soundDanceStabDance);
		event.getRegistry().register(soundRetroBattleTheme);
		event.getRegistry().register(soundWarhorn);
		event.getRegistry().register(soundWhispers);
		event.getRegistry().register(soundScreech);
		event.getRegistry().register(soundUpcheladder);
		
		event.getRegistry().register(soundNakagatorAmbient);
		event.getRegistry().register(soundNakagatorHurt);
		event.getRegistry().register(soundNakagatorDeath);
		event.getRegistry().register(soundIguanaAmbient);
		event.getRegistry().register(soundIguanaHurt);
		event.getRegistry().register(soundIguanaDeath);
		event.getRegistry().register(soundSalamanderAmbient);
		event.getRegistry().register(soundSalamanderHurt);
		event.getRegistry().register(soundSalamanderDeath);
		event.getRegistry().register(soundTurtleHurt);
		event.getRegistry().register(soundTurtleDeath);
		
		event.getRegistry().register(soundFrogAmbient);
		event.getRegistry().register(soundFrogHurt);
		event.getRegistry().register(soundFrogDeath);
		
		event.getRegistry().register(soundImpAmbient);
		event.getRegistry().register(soundImpHurt);
		event.getRegistry().register(soundImpDeath);
		event.getRegistry().register(soundOgreAmbient);
		event.getRegistry().register(soundOgreHurt);
		event.getRegistry().register(soundOgreDeath);
		event.getRegistry().register(soundBasiliskAmbient);
		event.getRegistry().register(soundBasiliskHurt);
		event.getRegistry().register(soundBasiliskDeath);
		event.getRegistry().register(soundLichAmbient);
		event.getRegistry().register(soundLichHurt);
		event.getRegistry().register(soundLichDeath);
		event.getRegistry().register(soundGiclopsAmbient);
		event.getRegistry().register(soundGiclopsHurt);
		event.getRegistry().register(soundGiclopsDeath);
	}
}
