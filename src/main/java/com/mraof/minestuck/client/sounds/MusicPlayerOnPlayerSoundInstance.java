package com.mraof.minestuck.client.sounds;

import com.mraof.minestuck.inventory.musicplayer.CapabilityMusicPlaying;
import com.mraof.minestuck.inventory.musicplayer.IMusicPlaying;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;


public class MusicPlayerOnPlayerSoundInstance extends AbstractTickableSoundInstance
{
	private final Player player;
	private final SoundEvent soundEvent;
	
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
		Optional<IMusicPlaying> musicCap = player.getItemInHand(player.getUsedItemHand())
				.getCapability(CapabilityMusicPlaying.MUSIC_PLAYING_CAPABILITY).resolve();

			if(musicCap.isPresent() && musicCap.get().getCurrentMusic() == soundEvent)
			{
				this.x = (float) this.player.getX();
				this.y = (float) this.player.getY();
				this.z = (float) this.player.getZ();
				return;
			}
			this.stop();
	}
}
