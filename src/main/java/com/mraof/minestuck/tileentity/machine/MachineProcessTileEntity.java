package com.mraof.minestuck.tileentity.machine;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiPredicate;

public abstract class MachineProcessTileEntity extends TileEntity implements ITickableTileEntity
{
	protected final ItemStackHandler itemHandler = createItemHandler();
	private final LazyOptional<IItemHandler> itemOptional = LazyOptional.of(() -> itemHandler);
	protected final IIntArray parameters = new ProgressIntArray(this);
	public static final int DEFAULT_MAX_PROGRESS = 100;

	public int progress = 0;
	public int maxProgress = DEFAULT_MAX_PROGRESS;
	public boolean ready = false;
	public boolean overrideStop = false;
	
	protected MachineProcessTileEntity(TileEntityType<?> tileEntityTypeIn)
	{
		super(tileEntityTypeIn);
	}
	
	protected abstract ItemStackHandler createItemHandler();
	
	public abstract RunType getRunType();
	
	public boolean getOverrideStop()
	{
		return getRunType() == RunType.BUTTON_OVERRIDE && overrideStop;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);

		this.progress = nbt.getInt("progress");
		if(getRunType() == RunType.BUTTON_OVERRIDE)
			this.overrideStop = nbt.getBoolean("overrideStop");
		if(nbt.contains("inventory", Constants.NBT.TAG_COMPOUND))
			itemHandler.deserializeNBT(nbt.getCompound("inventory"));
		else itemHandler.deserializeNBT(nbt);	//TODO reads save format from before the item handler. Remove when we don't care about backwards-compability to early mc1.15 versions
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);

		compound.putInt("progress", this.progress);
		if(getRunType() == RunType.BUTTON_OVERRIDE)
			compound.putBoolean("overrideStop", this.overrideStop);
		compound.put("inventory", itemHandler.serializeNBT());

		return compound;
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemOptional.cast();
		return super.getCapability(cap, side);
	}
	
	@Override
	public void tick()
	{
		BlockState state = level.getBlockState(worldPosition);
		if (level.isClientSide)    //Processing is easier done on the server side only
			return;

		if ((!ready && getRunType() != RunType.AUTOMATIC) || !contentsValid())
		{
			boolean b = progress == 0;
			this.progress = 0;
			this.ready = getOverrideStop();
			if (!b)
				level.sendBlockUpdated(worldPosition, state, state, 3);
			return;
		}

		this.progress++;

		if (this.progress >= this.maxProgress)
		{
			this.progress = 0;
			this.ready = getOverrideStop();
			processContents();
		}
	}

	public abstract boolean contentsValid();

	public abstract void processContents();
	
	public enum RunType
	{
		AUTOMATIC,
		BUTTON,
		BUTTON_OVERRIDE
	}
	
	protected class CustomHandler extends ItemStackHandler
	{
		private final BiPredicate<Integer, ItemStack> isValidPredicate;
		
		protected CustomHandler(int size)
		{
			this(size, (slot, stack) -> true);
		}
		
		protected CustomHandler(int size, BiPredicate<Integer, ItemStack> isValidPredicate)
		{
			super(size);
			this.isValidPredicate = isValidPredicate;
		}
		
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		{
			return isValidPredicate.test(slot, stack);
		}
		
		@Override
		protected void onContentsChanged(int slot)
		{
			MachineProcessTileEntity.this.setChanged();
		}
	}
	
	private static class ProgressIntArray implements IIntArray
	{
		private final MachineProcessTileEntity tileEntity;
		
		private ProgressIntArray(MachineProcessTileEntity tileEntity)
		{
			this.tileEntity = tileEntity;
		}
		
		@Override
		public int get(int index)
		{
			if(index == 0)
				return tileEntity.progress;
			else if(index == 1)
				return tileEntity.overrideStop ? 1 : 0;
			else if(index == 2)
				return tileEntity.ready ? 1 : 0;
			return 0;
		}
		
		@Override
		public void set(int index, int value)
		{
			if(index == 0)
				tileEntity.progress = value;
			else if(index == 1)
				tileEntity.overrideStop = value != 0;
			else if(index == 2)
				tileEntity.ready = value != 0;
		}
		
		@Override
		public int getCount()
		{
			return 3;
		}
	}
}