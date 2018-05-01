package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.BlockCruxtruder2;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCruxtruder extends TileEntity
{
	private int color = -1;
	private boolean broken = false;
	private boolean dowelOut = false;
	
	public int getColor()
	{
		return color;
	}
	
	public boolean IsDowelOut()
	{
		return dowelOut;
	}
	
	public void setColor(int Color)
	{
		color = Color;
	}
	
	public void setDowelOut(boolean isOut)
	{
		dowelOut = isOut;
		if(world != null)
		{
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 2);
		}
	}
	
	public boolean isBroken()
	{
		return broken;
	}
	public void destroy()
	{
		broken = true;
	}

	public void onRightClick(EntityPlayer player, IBlockState clickedState)
	{
		if(!isBroken())
		{
			if(clickedState.getBlock() == MinestuckBlocks.cruxtruder2
					&& clickedState.getValue(BlockCruxtruder2.PART) == BlockCruxtruder2.EnumParts.ONE_THREE_ONE
					&& !clickedState.getValue(BlockCruxtruder2.HASLID))
			{
				if(dowelOut)
				{
					if(!world.isRemote)
					{
						ItemStack dowel = new ItemStack(MinestuckItems.cruxiteDowel, 1, color + 1);
						EntityItem dowelEntity = new EntityItem(world, pos.getX(), pos.up().getY(), pos.getZ(), dowel);
						world.spawnEntity(dowelEntity);
					}
					setDowelOut(false);
				} else
				{
					setDowelOut(true);
				}
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		
		if(tagCompound.hasKey("color"))
			color = tagCompound.getInteger("color");
		if(tagCompound.hasKey("broken"))
			broken = tagCompound.getBoolean("broken");
		if(tagCompound.hasKey("dowel"))
			setDowelOut(tagCompound.getBoolean("dowel"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("color", color);
		tagCompound.setBoolean("broken", broken);
		tagCompound.setBoolean("dowel", dowelOut);
		return tagCompound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}
}
