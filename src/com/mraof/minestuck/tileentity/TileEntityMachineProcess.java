package com.mraof.minestuck.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public abstract class TileEntityMachineProcess extends TileEntity implements ISidedInventory, ITickable
{

	public int progress = 0;
	public int maxProgress = 100;
	public boolean ready = false;
	public boolean overrideStop = false;
	protected final NonNullList<ItemStack> inv;
	
	public TileEntityMachineProcess(TileEntityType<?> tileEntityTypeIn)
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
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return this.world.getTileEntity(pos) == this &&
				player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) < 64;
	}
	
	@Override
	public void read(NBTTagCompound compound)
	{
		super.read(compound);

		this.progress = compound.getInt("progress");
		if(getRunType() == RunType.BUTTON_OVERRIDE)
			this.overrideStop = compound.getBoolean("overrideStop");
		inv.clear();
		ItemStackHelper.loadAllItems(compound, inv);
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		super.write(compound);

		compound.setInt("progress", this.progress);
		if(getRunType() == RunType.BUTTON_OVERRIDE)
			compound.setBoolean("overrideStop", this.overrideStop);
		ItemStackHelper.saveAllItems(compound, inv);

		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.write(new NBTTagCompound());
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound tagCompound = this.getUpdateTag();
		return new SPacketUpdateTileEntity(this.pos, 2, tagCompound);
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		this.read(tag);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.handleUpdateTag(pkt.getNbtCompound());
	}
	
	@Override
	public void tick()
	{
		IBlockState state = world.getBlockState(pos);
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
	
	@Nullable
	@Override
	public ITextComponent getCustomName()
	{
		return null;
	}
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}
	
	@Override
	public void openInventory(EntityPlayer playerIn)
	{
	}

	@Override
	public void closeInventory(EntityPlayer playerIn)
	{
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		inv.clear();
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable EnumFacing direction)
	{
		return isItemValidForSlot(index, itemStackIn);
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return true;
	}
	
	public enum RunType
	{
		AUTOMATIC,
		BUTTON,
		BUTTON_OVERRIDE
	}
}