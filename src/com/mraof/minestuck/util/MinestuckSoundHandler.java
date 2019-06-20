package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MinestuckSoundHandler
{
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
		soundLocation = new ResourceLocation("minestuck", "record.dance_stab");
		soundDanceStabDance = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","record.retro_battle");
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
		soundLocation = new ResourceLocation("minestuck","nakagator_ambient");
		soundNakagatorAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","nakagator_hurt");
		soundNakagatorHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","nakagator_death");
		soundNakagatorDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Iguanas
		soundLocation = new ResourceLocation("minestuck","iguana_ambient");
		soundIguanaAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","iguana_hurt");
		soundIguanaHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","iguana_death");
		soundIguanaDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Salamanders
		soundLocation = new ResourceLocation("minestuck","salamander_ambient");
		soundSalamanderAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","salamander_hurt");
		soundSalamanderHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","salamander_death");
		soundSalamanderDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Turtles
		soundLocation = new ResourceLocation("minestuck","turtle_hurt");
		soundTurtleHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","turtle_death");
		soundTurtleDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		
		//Frogs
		soundLocation = new ResourceLocation("minestuck","frog_ambient");
		soundFrogAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","frog_hurt");
		soundFrogHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck","frog_death");
		soundFrogDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		
		//Imps
		soundLocation = new ResourceLocation("minestuck", "imp_ambient");
		soundImpAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "imp_hurt");
		soundImpHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "imp_death");
		soundImpDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Ogres
		soundLocation = new ResourceLocation("minestuck", "ogre_ambient");
		soundOgreAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "ogre_hurt");
		soundOgreHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "ogre_death");
		soundOgreDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Basilisks
		soundLocation = new ResourceLocation("minestuck", "basilisk_ambient");
		soundBasiliskAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "basilisk_hurt");
		soundBasiliskHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "basilisk_death");
		soundBasiliskDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Liches
		soundLocation = new ResourceLocation("minestuck", "lich_ambient");
		soundLichAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "lich_hurt");
		soundLichHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "lich_death");
		soundLichDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		//Giclops
		soundLocation = new ResourceLocation("minestuck", "giclops_ambient");
		soundGiclopsAmbient = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "giclops_hurt");
		soundGiclopsHurt = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		soundLocation = new ResourceLocation("minestuck", "giclops_death");
		soundGiclopsDeath = new SoundEvent(soundLocation).setRegistryName(soundLocation);
	}
	
	@SubscribeEvent
	public static void registerSound(final RegistryEvent.Register<SoundEvent> event)
	{
		MinestuckSoundHandler.initSound();
		
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
