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

public class CapabilityProviderMusicPlayer implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
	private final LazyOptional<IItemHandler> lazyInitialisionSupplier = LazyOptional.of(this::getCachedInventory);
	private ItemStackHandlerMusicPlayer itemStackHandlerMusicPlayer;
	
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == cap) return (LazyOptional<T>) (lazyInitialisionSupplier);
		return LazyOptional.empty();
	}
	
	private @NotNull ItemStackHandlerMusicPlayer getCachedInventory() {
		if (itemStackHandlerMusicPlayer == null) {
			itemStackHandlerMusicPlayer = new ItemStackHandlerMusicPlayer(1);
		}
		return itemStackHandlerMusicPlayer;
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

