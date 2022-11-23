package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.inventory.MSCapabilities;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MusicPlayingCapabilityProvider implements ICapabilityProvider
{
	private final LazyOptional<IMusicPlaying> lazyInitSupplierMusicPlaying = LazyOptional.of(this::getCachedMusicPlaying);
	private MusicPlaying musicPlaying;
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(MSCapabilities.MUSIC_PLAYING_CAPABILITY == cap)
			return (lazyInitSupplierMusicPlaying).cast();
		return LazyOptional.empty();
	}
	
	private @Nonnull MusicPlaying getCachedMusicPlaying()
	{
		if(musicPlaying == null)
		{
			musicPlaying = new MusicPlaying();
		}
		return musicPlaying;
	}
}

