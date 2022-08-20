package com.mraof.minestuck.inventory.musicplayer;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityMusicPlaying
{
	public static final Capability<IMusicPlaying> MUSIC_PLAYING_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
	
	public static void register(RegisterCapabilitiesEvent event)
	{
		event.register(IMusicPlaying.class);
	}
	
}
