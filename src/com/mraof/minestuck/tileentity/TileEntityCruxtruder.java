package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.BlockCruxtiteDowel;
import com.mraof.minestuck.block.BlockCruxtruder;
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
import net.minecraft.util.math.BlockPos;

public class TileEntityCruxtruder extends TileEntity
{
	private int color = -1;
	private boolean broken = false;
	
	public int getColor()
	{
		return color;
	}
	
	public void setColor(int Color)
	{
		color = Color;
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
			if(clickedState.getValue(BlockCruxtruder.PART) == BlockCruxtruder.EnumParts.TUBE)
			{
				BlockPos pos = getPos().up();
				IBlockState state = getWorld().getBlockState(pos);
				if(state.getBlock() == MinestuckBlocks.cruxiteDowel)
				{
					BlockCruxtiteDowel.dropDowel(getWorld(), pos);
				} else if(state.getBlock().isReplaceable(getWorld(), pos))
				{
					world.setBlockState(pos, MinestuckBlocks.cruxiteDowel.getDefaultState());
					TileEntity te = world.getTileEntity(pos);
					if(te instanceof TileEntityItemStack)
						((TileEntityItemStack) te).getStack().setItemDamage(color + 1);
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
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("color", color);
		tagCompound.setBoolean("broken", broken);
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
