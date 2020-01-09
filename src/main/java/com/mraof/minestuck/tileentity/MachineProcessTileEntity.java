package com.mraof.minestuck.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;

public abstract class MachineProcessTileEntity extends TileEntity implements ISidedInventory, ITickableTileEntity
{
	protected final IIntArray parameters = new ProgressIntArray(this);
	public static final int DEFAULT_MAX_PROGRESS = 100;

	public int progress = 0;
	public int maxProgress = DEFAULT_MAX_PROGRESS;
	public boolean ready = false;
	public boolean overrideStop = false;
	protected final NonNullList<ItemStack> inv;
	
	public MachineProcessTileEntity(TileEntityType<?> tileEntityTypeIn)
	{
		super(tileEntityTypeIn);
		inv = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	}
	
	public abstract RunType getRunType();
	
	public boolean getOverrideStop()
	{
		return getRunType() == RunType.BUTTON_OVERRIDE && overrideStop;
	}
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return index >= this.getSizeInventory() || index < 0 ? ItemStack.EMPTY : this.inv.get(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.inv.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit())
		{
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(this.inv, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.inv, index);
	}

	
	
	@Override
	public boolean isEmpty()
	{
		for (ItemStack stack : inv)
			if (!stack.isEmpty())
				return false;
		return true;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public boolean isUsableByPlayer(PlayerEntity player)
	{
		return this.world.getTileEntity(pos) == this &&
				player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) < 64;
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);

		this.progress = compound.getInt("progress");
		if(getRunType() == RunType.BUTTON_OVERRIDE)
			this.overrideStop = compound.getBoolean("overrideStop");
		inv.clear();
		ItemStackHelper.loadAllItems(compound, inv);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);

		compound.putInt("progress", this.progress);
		if(getRunType() == RunType.BUTTON_OVERRIDE)
			compound.putBoolean("overrideStop", this.overrideStop);
		ItemStackHelper.saveAllItems(compound, inv);

		return compound;
	}
	
	@Override
	public void tick()
	{
		BlockState state = world.getBlockState(pos);
		if (world.isRemote)    //Processing is easier done on the server side only
			return;

		if ((!ready && getRunType() != RunType.AUTOMATIC) || !contentsValid())
		{
			boolean b = progress == 0;
			this.progress = 0;
			this.ready = getOverrideStop();
			if (!b)
				world.notifyBlockUpdate(pos, state, state, 3);
			return;
		}

		this.progress++;

		if (this.progress == this.maxProgress)
		{
			this.progress = 0;
			this.ready = getOverrideStop();
			processContents();
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	public abstract boolean contentsValid();

	public abstract void processContents();
	
	@Override
	public void clear()
	{
		inv.clear();
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction)
	{
		return isItemValidForSlot(index, itemStackIn);
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction)
	{
		return true;
	}
	
	public enum RunType
	{
		AUTOMATIC,
		BUTTON,
		BUTTON_OVERRIDE
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
		public int size()
		{
			return 3;
		}
	}
}