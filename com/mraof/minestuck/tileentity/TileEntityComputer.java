package com.mraof.minestuck.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IConnectionListener;
import com.mraof.minestuck.util.SburbConnection;

public class TileEntityComputer extends TileEntity implements IConnectionListener {

	public int program;
	public String connectedTo = "";
	public boolean givenItems = false;
	public String connectedFrom = "";
	public boolean initialized = false;
	public GuiComputer gui;
	public String owner = "";
	private Minecraft mc = Minecraft.getMinecraft();
	
    public TileEntityComputer() {
            SburbConnection.addListener(this);
            if (Minecraft.getMinecraft().thePlayer != null) {
            	owner = Minecraft.getMinecraft().thePlayer.username;
            }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
    	super.readFromNBT(par1NBTTagCompound);
    	 this.program = par1NBTTagCompound.getInteger("program");
    	 this.connectedTo = par1NBTTagCompound.getString("connectedTo");
    	 this.connectedFrom = par1NBTTagCompound.getString("conectedFrom");
    	 this.owner = par1NBTTagCompound.getString("owner");
    	 this.givenItems = par1NBTTagCompound.getBoolean("givenItems");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setInteger("program",this.program);
    	par1NBTTagCompound.setBoolean("givenItems",this.givenItems);
    	if (!this.connectedTo.equals("")) {
    		par1NBTTagCompound.setString("connectedTo",this.connectedTo);
    	}
    	if (!this.connectedFrom.equals("")) {
    		par1NBTTagCompound.setString("conectedFrom",this.connectedFrom);
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
    }
    
    @Override
    public void updateEntity() {
    	if (!initialized) {
    		updateConnection();
    		initialized = true;
    	}
    }
    
    public void updateConnection() {
    	if (gui == null) {
    		//We're server side
    		
    	} else {
    		//We're client side
    		
        	if (!connectedTo.equals("") && !connectedFrom.equals("")) {
        		if (program == 0) {
        			
        			SburbConnection.connect(connectedFrom,connectedTo);
        			
        			Packet250CustomPayload packet = new Packet250CustomPayload();
        			packet.channel = "Minestuck";
        			packet.data = MinestuckPacket.makePacket(Type.SBURB_CONNECT,connectedFrom,connectedTo);
        			packet.length = packet.data.length;
        			mc.getNetHandler().addToSendQueue(packet);
        			
        		} else {
        			
        		}
    			
    		} else if (connectedTo.equals("") && !connectedFrom.equals("")) {
    	  		if (program == 1) {
    				
        			Packet250CustomPayload packet = new Packet250CustomPayload();
        			packet.channel = "Minestuck";
        			packet.data = MinestuckPacket.makePacket(Type.SBURB_OPEN,connectedFrom);
        			packet.length = packet.data.length;
        			mc.getNetHandler().addToSendQueue(packet);
    	  		} else {
    	  			
    	  		}
    		}
        	
    		gui.updateGui();
    	}
    }

	@Override
	public void onConnected(String server, String client) {
		if (server == connectedFrom && program == 1) {
				this.connectedTo = client;
				this.connectedFrom = server;
				updateConnection();
	
				if (gui != null) {
					gui.updateGui();
				}
			} else if (owner == client && program == 0) {
				this.connectedTo = server;
				this.connectedFrom = client;
		}
	}

	@Override
	public void onServerOpen(String server) {
		if (server == this.owner && program == 1) {
			this.connectedFrom = server;
			if (gui != null) {
				gui.updateGui();
			}
		}
	}
    
}
