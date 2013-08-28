package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IConnectionListener;
import com.mraof.minestuck.util.SburbConnection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityComputer extends TileEntity implements IConnectionListener {

	public int program;
	public String connectedTo = "";
	public boolean givenItems = false;
	//public boolean waiting = false;
	public String owner = "";
	public boolean initialized = false;
	public SburbConnection conn;
	public GuiComputer gui;
	
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
    	//updateConnection();
    }
    
    @Override
    public void updateEntity() {
    	if (!initialized) {
    		updateConnection();
    		initialized = true;
    	}
    }
    
    public void updateConnection() {
    	if (!connectedTo.equals("") && !owner.equals("")) {
			conn = new SburbConnection(this);
			conn.connect(connectedTo);
		} else if (connectedTo.equals("") && !owner.equals("")) {
	  		if (program == 1) {
				conn = new SburbConnection(this);
				conn.openServer(owner);
	  		}
		} else {
			conn = new SburbConnection(this);
		}
    	if (gui != null) {
    		gui.updateGui();
    	}
    }

	@Override
	public void onConnected(SburbConnection conn) {
		// TODO Auto-generated method stub
		this.connectedTo = conn.client;
		updateConnection();
	}
    
}
