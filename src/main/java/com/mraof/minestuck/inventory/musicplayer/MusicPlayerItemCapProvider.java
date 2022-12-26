package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.item.weapon.MusicPlayerWeapon;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A capability provider that exposes an item handler capability with one item slot.
 *
 * @see MusicPlayerWeapon
 */

public class MusicPlayerItemCapProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
	private final LazyOptional<IItemHandler> lazyInitSupplierItemHandler = LazyOptional.of(this::getCachedInventory);
	private ItemStackHandler itemHandler;
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == cap)
			return (lazyInitSupplierItemHandler).cast();
		return LazyOptional.empty();
	}
	
	private @Nonnull ItemStackHandler getCachedInventory()
	{
		if(itemHandler == null)
		{
			itemHandler = new ItemStackHandler(1);
		}
		return itemHandler;
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

