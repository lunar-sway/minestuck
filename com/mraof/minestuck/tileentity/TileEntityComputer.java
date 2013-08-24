package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.SburbConnection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityComputer extends TileEntity {

	public int program;
	public String connectedTo = "";
	public boolean givenItems = false;
	//public boolean waiting = false;
	public String owner = "";
	public boolean initialized = false;
	public SburbConnection conn;
	
    public TileEntityComputer() {
            
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
    	super.readFromNBT(par1NBTTagCompound);
    	 this.program = par1NBTTagCompound.getInteger("program");
    	 this.connectedTo = par1NBTTagCompound.getString("connectedTo");
    	 this.owner = par1NBTTagCompound.getString("owner");
    	 this.givenItems = par1NBTTagCompound.getBoolean("givenItems");
    	//this.connected = connectedTo != "";
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setInteger("program",this.program);
    	par1NBTTagCompound.setBoolean("givenItems",this.givenItems);
    	if (!this.connectedTo.equals("")) {
    		par1NBTTagCompound.setString("connectedTo",this.connectedTo);
    	}
    	if (!this.owner.equals("")) {
    		par1NBTTagCompound.setString("owner",this.owner);
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
    	Debug.print("Data packet gotten "+net.getClass());
    	this.readFromNBT(pkt.customParam1);
    	if (conn == null) {
	   		if (connectedTo.equals("") && !owner.equals("") && program == 1) {
				conn = new SburbConnection(owner,false);
				SburbConnection.addServer(conn);
			} else if (connectedTo.equals("") && !owner.equals("") && program == 0) {
	   			conn = new SburbConnection(owner,true);
			} else if (!connectedTo.equals("") && !owner.equals("")) {
				conn = new SburbConnection(owner,program == 0);
				conn.connect(connectedTo);
			} 
    	}
    }
    
    @Override
    public void updateEntity() {
    	if (!initialized) {
    		if (connectedTo.equals("") && !owner.equals("") && program == 1) {
    			conn = new SburbConnection(owner,false);
    			SburbConnection.addServer(conn);
    			initialized = true;
    		} else if (connectedTo.equals("") && !owner.equals("") && program == 0) {
       			conn = new SburbConnection(owner,true);
       			initialized = true;
    		} else if (!connectedTo.equals("") && !owner.equals("")) {
    			conn = new SburbConnection(owner,program == 0);
    			conn.connect(connectedTo);
    			initialized = true;
    		} 
    	}
    }
    
}
