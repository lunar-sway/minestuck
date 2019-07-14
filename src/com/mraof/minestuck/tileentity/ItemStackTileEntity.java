package com.mraof.minestuck.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class ItemStackTileEntity extends TileEntity
{
	public ItemStackTileEntity()
	{
		super(ModTileEntityTypes.ITEM_STACK);
	}
	
	private ItemStack stack = ItemStack.EMPTY;
	
	public ItemStack getStack()
	{
		return stack;
	}
	
	public void setStack(ItemStack stack)
	{
		if(stack != null)
		{
			this.stack = stack;
			markDirty();
		}
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		stack = ItemStack.read(compound.getCompound("stack"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound = super.write(compound);
		compound.put("stack", stack.write(new CompoundNBT()));
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return write(new CompoundNBT());
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(getPos(), 2, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
		if(world != null)
		{
			BlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 2);
		}
	}
}