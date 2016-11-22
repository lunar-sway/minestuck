package com.mraof.minestuck.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public abstract class TileEntityMachine extends TileEntity implements IInventory, ITickable
{
	
	protected ItemStack[] inv = new ItemStack[4];
	public int progress = 0;
	public int maxProgress = 100;
	public boolean ready = false;
	public boolean overrideStop = false;
	
	protected abstract boolean isAutomatic();
	protected abstract boolean allowOverrideStop();
	
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.inv[slot];
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		this.inv[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit())
		{
			stack.stackSize = getInventoryStackLimit();
		}
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amt)
	{
		ItemStack stack = getStackInSlot(slot);
		if (stack != null)
		{
			if (stack.stackSize <= amt)
				setInventorySlotContents(slot, null);
			else
			{
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0)
					setInventorySlotContents(slot, null);
			}
		}
		return stack;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		ItemStack stack = getStackInSlot(index);
		if (stack != null)
			setInventorySlotContents(index, null);
		return stack;
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
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		
		this.progress = tagCompound.getInteger("progress");
		this.overrideStop = tagCompound.getBoolean("overrideStop");
		
		for (int i = 0; i < inv.length; i++)
		{
			NBTTagCompound tag = tagCompound.getCompoundTag("slot"+i);
			this.inv[i] = ItemStack.loadItemStackFromNBT(tag);
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("progress", this.progress);
		tagCompound.setBoolean("overrideStop", this.overrideStop);
		
		for (int i = 0; i < this.inv.length; i++)
		{
			ItemStack stack = this.inv[i];
			NBTTagCompound tag = new NBTTagCompound();
			if (stack != null)
				stack.writeToNBT(tag);
			tagCompound.setTag("slot"+i, tag);
		}
		return tagCompound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.writeToNBT(new NBTTagCompound());
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
		this.readFromNBT(tag);
		
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) 
	{
		this.handleUpdateTag(pkt.getNbtCompound());
	}
	
	@Override
	public void update()
	{
		IBlockState state = world.getBlockState(pos);
		if(world.isRemote)	//Processing is easier done on the server side only
			return;
		
		if ((!ready && !isAutomatic()) || !contentsValid())
		{
			boolean b = progress == 0;
			this.progress = 0;
			this.ready = overrideStop;
			if(!b)
				world.notifyBlockUpdate(pos, state, state, 3);
			return;
		}
		
		this.progress++;
		
		if (this.progress == this.maxProgress)
		{
			this.progress = 0;
			this.ready = overrideStop;
			processContents();
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}
	
	public abstract boolean contentsValid();
	
	public abstract void processContents();
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation(getName());
	}

	@Override
	public void openInventory(EntityPlayer playerIn)
	{}
	
	@Override
	public void closeInventory(EntityPlayer playerIn)
	{}
	
	@Override
	public int getField(int id)
	{
		return 0;
	}
	
	@Override
	public void setField(int id, int value)
	{}
	
	@Override
	public int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public void clear()
	{
		inv = new ItemStack[4];
	}
	
}