package com.mraof.minestuck.client.sounds;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;


public class MusicPlayerOnPlayerSoundInstance extends AbstractTickableSoundInstance
{
	private final Player player;
	private final SoundEvent soundEvent;
	
	private static Map<Integer, MusicPlayerOnPlayerSoundInstance> entitiesMap = new HashMap<>();
	
	public MusicPlayerOnPlayerSoundInstance(Player player, SoundEvent musicPlaying)
	{
		super(musicPlaying, SoundSource.PLAYERS);
		this.looping = false;
		this.delay = 0;
		this.volume = 10.0F;
		this.player = player;
		this.soundEvent = musicPlaying;
	}
	
	public void tick()
	{
			this.x = (float) this.player.getX();
			this.y = (float) this.player.getY();
			this.z = (float) this.player.getZ();
	}
	
	public static Map<Integer, MusicPlayerOnPlayerSoundInstance> getEntitiesMap()
	{
		return entitiesMap;
	}
}
