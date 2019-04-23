package com.mraof.minestuck.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class TileEntityItemStack extends TileEntity
{
	public TileEntityItemStack()
	{
		super(MinestuckTiles.ITEM_STACK);
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
		}
	}
	
	@Override
	public void read(NBTTagCompound compound)
	{
		super.read(compound);
		stack = ItemStack.read(compound.getCompound("stack"));
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		compound = super.write(compound);
		compound.setTag("stack", stack.write(new NBTTagCompound()));
		return compound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return write(new NBTTagCompound());
	}
	
	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(getPos(), 2, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
		if(world != null)
		{
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 2);
		}
	}
}