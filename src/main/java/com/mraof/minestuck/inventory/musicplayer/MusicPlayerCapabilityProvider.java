package com.mraof.minestuck.inventory.musicplayer;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MusicPlayerCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
	private final LazyOptional<IItemHandler> lazyInitSupplierItemHandler = LazyOptional.of(this::getCachedInventory);
	private final LazyOptional<IMusicPlaying> lazyInitSupplierMusicPlaying = LazyOptional.of(this::getCachedMusicPlaying);
	private ItemStackHandlerMusicPlayer itemStackHandlerMusicPlayer;
	private MusicPlaying musicPlaying;
	
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == cap)
			return (lazyInitSupplierItemHandler).cast();
		if(CapabilityMusicPlaying.MUSIC_PLAYING_CAPABILITY == cap)
			return (lazyInitSupplierMusicPlaying).cast();
		return LazyOptional.empty();
	}
	
	private @NotNull ItemStackHandlerMusicPlayer getCachedInventory() {
		if (itemStackHandlerMusicPlayer == null) {
			itemStackHandlerMusicPlayer = new ItemStackHandlerMusicPlayer(1);
		}
		return itemStackHandlerMusicPlayer;
	}
	
	private @NotNull MusicPlaying getCachedMusicPlaying() {
		if (musicPlaying == null) {
			musicPlaying = new MusicPlaying();
		}
		return musicPlaying;
	}
	
	@Override
	public CompoundTag serializeNBT()
	{
		return getCachedInventory().serializeNBT();
	}
	
	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		getCachedInventory().deserializeNBT(nbt);
	}
}

