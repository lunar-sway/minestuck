package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.item.weapon.MusicPlayerWeapon;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

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
		return Capabilities.ITEM_HANDLER.orEmpty(cap, lazyInitSupplierItemHandler);
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

