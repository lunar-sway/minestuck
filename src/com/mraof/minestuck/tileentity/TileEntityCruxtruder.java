package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.BlockCruxtruder2;
import com.mraof.minestuck.block.BlockPunchDesignix;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import scala.util.Random;

public class TileEntityCruxtruder extends TileEntity
{
	private int color = -1;
	private boolean destroyed=false;
	private boolean dowelOut=false;
	
	
	public int getColor(){
		return color;
	}
	public boolean IsDowelOut() {
		return dowelOut;
	}
	public void setColor(int Color){
		color = Color;
	}
	public void setDowelOut(boolean state){
		dowelOut=state;
		resendState();
	}
	public boolean isDestroyed(){
		return destroyed;
	}
	public void destroy(){
		destroyed=true;
	}

	public void onRightClick(EntityPlayer player, IBlockState clickedState) {
		if(!isDestroyed()) {
			if(clickedState.getBlock()==MinestuckBlocks.cruxtruder2
					&& clickedState.getValue(BlockCruxtruder2.PART)== BlockCruxtruder2.EnumParts.ONE_THREE_ONE
					&& clickedState.getValue(BlockCruxtruder2.HASLID)==false)
			{
				if(dowelOut) {
					if(!world.isRemote) {
						ItemStack dowel=new ItemStack(MinestuckItems.cruxiteDowel, 1, color + 1);
						EntityItem dowelEntity =new EntityItem(world, pos.getX(), pos.up().getY(), pos.getZ(), dowel);
						world.spawnEntity(dowelEntity);
					}
					setDowelOut(false);
				}else {
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
			this.color = tagCompound.getInteger("color");
		if(tagCompound.hasKey("destroyed")) 
			this.destroyed=tagCompound.getBoolean("destroyed");
		if(tagCompound.hasKey("dowelOut"))
			this.dowelOut=tagCompound.getBoolean("dowelout");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("color", color);
		tagCompound.setBoolean("destroyed", destroyed);
		tagCompound.setBoolean("dowelOut", dowelOut);
		return tagCompound;
	}
	@Override
	public NBTTagCompound getUpdateTag(){
		NBTTagCompound nbt;
		nbt = super.getUpdateTag();
		nbt.setBoolean("destroyed", destroyed);
		return nbt;
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		SPacketUpdateTileEntity packet;
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("destroyed",destroyed);
		//dowel.writeToNBT(nbt);
		packet = new SPacketUpdateTileEntity(this.pos, 0, nbt);				
		return packet;
	}
	
	public void resendState()
	{
		if(!dowelOut)
		{
			BlockCruxtruder2.updateItem(false, world, this.getPos());
		} else
		{
			BlockCruxtruder2.updateItem(true, world, this.getPos());
		}
	}
}
