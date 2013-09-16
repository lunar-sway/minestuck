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
	
	public volatile boolean hasClient = false;
	public volatile boolean hasServer = false;
	public boolean openToClients = false;
	public String connectedClient = "";
	public boolean givenItems = false;
	public String connectedServer = "";
	public boolean initialized = false;
	public GuiComputer gui;
	public String owner = "";
	private Minecraft mc = Minecraft.getMinecraft();
	public int programSelected = -1;	//0 if client is selected, 1 if server.
	
    public TileEntityComputer() {
            SburbConnection.addListener(this);
            if (Minecraft.getMinecraft().thePlayer != null) {
            	owner = Minecraft.getMinecraft().thePlayer.username;
            }
    }
    
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.hasClient = par1NBTTagCompound.getBoolean("hasClient");
		this.hasServer = par1NBTTagCompound.getBoolean("hasServer");
		this.connectedClient = par1NBTTagCompound.getString("client");
    	 this.connectedServer = par1NBTTagCompound.getString("server");
    	 this.owner = par1NBTTagCompound.getString("owner");
    	 this.givenItems = par1NBTTagCompound.getBoolean("givenItems");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setBoolean("hasClient",this.hasClient);
    	par1NBTTagCompound.setBoolean("hasServer",this.hasServer);
    	par1NBTTagCompound.setBoolean("givenItems",this.givenItems);
    	if (!this.connectedClient.equals("")) {
    		par1NBTTagCompound.setString("client",this.connectedClient);
    	}
    	if (!this.connectedServer.equals("")) {
    		par1NBTTagCompound.setString("server",this.connectedServer);
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
		if (programSelected == 0) {
			
			SburbConnection.connect(owner,connectedServer);
			
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_CONNECT,connectedServer,owner);
			packet.length = packet.data.length;
			mc.getNetHandler().addToSendQueue(packet);
			
			
		} else if (programSelected == 1) {
			if(connectedClient.equals("")){
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "Minestuck";
				packet.data = MinestuckPacket.makePacket(Type.SBURB_OPEN,connectedServer);
				packet.length = packet.data.length;
				mc.getNetHandler().addToSendQueue(packet);
				openToClients = true;
			}
		}
        	
    	if(gui != null)
    		gui.updateGui();
	}

	@Override
	public void onConnected(String server, String client) {
		if (owner == server && hasServer && openToClients) {
				this.connectedClient = client;
				openToClients = false;
				
				if (gui != null) {
					gui.updateGui();
				}
		} else if (owner == client && hasClient && this.connectedServer.isEmpty()) {
			this.connectedServer = server;
		}
	}

	@Override
	public void onServerOpen(String server) {
		if (gui != null && programSelected == 0) {
			gui.updateGui();
		}
	}
    
}
