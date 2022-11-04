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

public class ItemHandlerCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
	private final LazyOptional<IItemHandler> lazyInitSupplierItemHandler = LazyOptional.of(this::getCachedInventory);
	private CassetteItemHandler cassetteItemHandler;
	
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == cap)
			return (lazyInitSupplierItemHandler).cast();
		return LazyOptional.empty();
	}
	
	private @NotNull CassetteItemHandler getCachedInventory() {
		if (cassetteItemHandler == null) {
			cassetteItemHandler = new CassetteItemHandler(1);
		}
		return cassetteItemHandler;
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

