package com.mraof.minestuck.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityComputer extends TileEntity {

	public int program;
	public boolean connected;
	public String connectedTo;
	public boolean givenItems = false;
	public boolean waiting = false;
	
    public TileEntityComputer() {
            
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
    	super.readFromNBT(par1NBTTagCompound);
    	 this.program = par1NBTTagCompound.getInteger("program");
    	 this.connectedTo = par1NBTTagCompound.getString("connectedTo");
    	 this.givenItems = par1NBTTagCompound.getBoolean("givenItems");
    	 this.connected = connectedTo != "";
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setInteger("program",this.program);
    	par1NBTTagCompound.setBoolean("givenItems",this.givenItems);
    	if (this.connectedTo != null && !this.connectedTo.equals("")) {
    		par1NBTTagCompound.setString("connectedTo",this.connectedTo);
    	}

    }
    
    @Override
    public Packet getDescriptionPacket() 
    {
    	NBTTagCompound tagCompound = new NBTTagCompound();
    	this.writeToNBT(tagCompound);
    	return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 2, tagCompound);
    }
    
    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) 
    {
    	this.readFromNBT(pkt.customParam1);
    }
    
}
