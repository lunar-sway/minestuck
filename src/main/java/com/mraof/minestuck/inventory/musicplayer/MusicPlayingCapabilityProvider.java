package com.mraof.minestuck.inventory.musicplayer;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MusicPlayingCapabilityProvider implements ICapabilityProvider
{
	private final LazyOptional<IMusicPlaying> lazyInitSupplierMusicPlaying = LazyOptional.of(this::getCachedMusicPlaying);
	private MusicPlaying musicPlaying;
	
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(CapabilityMusicPlaying.MUSIC_PLAYING_CAPABILITY == cap)
			return (lazyInitSupplierMusicPlaying).cast();
		return LazyOptional.empty();
	}
	
	private @NotNull MusicPlaying getCachedMusicPlaying()
	{
		if(musicPlaying == null)
		{
			musicPlaying = new MusicPlaying();
		}
		return musicPlaying;
	}
}

