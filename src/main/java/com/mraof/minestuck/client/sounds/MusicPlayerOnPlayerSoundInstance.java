package com.mraof.minestuck.client.sounds;

import com.mraof.minestuck.inventory.musicplayer.IMusicPlaying;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;


public class MusicPlayerOnPlayerSoundInstance extends AbstractTickableSoundInstance
{
	private final Player player;
	private final IMusicPlaying soundEvent;
	
	public MusicPlayerOnPlayerSoundInstance(Player player, IMusicPlaying musicPlaying)
	{
		super(musicPlaying.getCurrentMusic(), SoundSource.PLAYERS);
		this.looping = false;
		this.delay = 0;
		this.volume = 10.0F;
		this.player = player;
		this.soundEvent = musicPlaying;
	}
	
	public void tick()
	{ //TODO if item is removed, stop playing
		if(soundEvent.getCurrentMusic() == null)
		{
			this.stop();
			
		} else
		{
			this.x = (float) this.player.getX();
			this.y = (float) this.player.getY();
			this.z = (float) this.player.getZ();
		}
	}
}
