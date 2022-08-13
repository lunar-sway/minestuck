package com.mraof.minestuck.client.sounds;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;


public class MusicPlayerOnPlayerSoundInstance extends AbstractTickableSoundInstance
{
	private final Player player;
	private int time;
	
	public MusicPlayerOnPlayerSoundInstance(Player pPlayer, SoundEvent sound) {
		super(sound, SoundSource.PLAYERS);
		this.player = pPlayer;
		this.looping = false;
		this.delay = 0;
		this.volume = 10.0F;
	}
	
	public void tick() {
		++this.time;
		if (!this.player.isRemoved() && this.time <= 20) {
			this.x = (float)this.player.getX();
			this.y = (float)this.player.getY();
			this.z = (float)this.player.getZ();
			float f = (float)this.player.getDeltaMovement().lengthSqr();
			if ((double)f >= 1.0E-7D) {
				this.volume = Mth.clamp(f / 4.0F, 0.0F, 1.0F);
			} else {
				this.volume = 0.0F;
			}
			
			if (this.time < 20) {
				this.volume = 0.0F;
			} else if (this.time < 40) {
				this.volume *= (float)(this.time - 20) / 20.0F;
			}
			
			float f1 = 0.8F;
			if (this.volume > 0.8F) {
				this.pitch = 1.0F + (this.volume - 0.8F);
			} else {
				this.pitch = 1.0F;
			}
			
		} else {
			this.stop();
		}
	}
	
	public void stopMusic() {
		this.stop();
	}
}
