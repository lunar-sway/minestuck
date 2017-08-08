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
	
	@SubscribeEvent
	public void registerSound(RegistryEvent.Register<SoundEvent> event)
	{
		ResourceLocation soundLocation = new ResourceLocation("minestuck", "record.emissary");
		soundEmissaryOfDance = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		event.getRegistry().register(soundEmissaryOfDance);
		soundLocation = new ResourceLocation("minestuck", "record.danceStab");
		soundDanceStabDance = new SoundEvent(soundLocation).setRegistryName(soundLocation);
		event.getRegistry().register(soundDanceStabDance);
	}
}
