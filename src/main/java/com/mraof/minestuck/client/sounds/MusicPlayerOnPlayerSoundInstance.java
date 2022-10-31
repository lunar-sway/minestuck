package com.mraof.minestuck.client.sounds;

import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;


public class MusicPlayerOnPlayerSoundInstance extends EntityBoundSoundInstance
{
	private static Map<Integer, MusicPlayerOnPlayerSoundInstance> entitiesMap = new HashMap<>();
	
	public MusicPlayerOnPlayerSoundInstance(Player player, SoundEvent musicPlaying)
	{
		super(musicPlaying, SoundSource.PLAYERS, 10.0F, 0.0F, player);
	}
	
	public static Map<Integer, MusicPlayerOnPlayerSoundInstance> getEntitiesMap()
	{
		return entitiesMap;
	}
}
