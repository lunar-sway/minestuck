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
		int program = par1NBTTagCompound.getInteger("program");
		hasClient =  (program & 1 << 0) != 0;
		hasServer =  (program & 1 << 1) != 0;
		this.connectedClient = par1NBTTagCompound.getString("connectedTo");
    	 this.connectedServer = par1NBTTagCompound.getString("conectedFrom");
    	 this.owner = par1NBTTagCompound.getString("owner");
    	 this.givenItems = par1NBTTagCompound.getBoolean("givenItems");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setInteger("program",(hasClient ? 2 : 0) + (hasServer ? 1 : 0));
    	par1NBTTagCompound.setBoolean("givenItems",this.givenItems);
    	if (!this.connectedClient.equals("")) {
    		par1NBTTagCompound.setString("connectedTo",this.connectedClient);
    	}
    	if (!this.connectedServer.equals("")) {
    		par1NBTTagCompound.setString("conectedFrom",this.connectedServer);
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
    	if (!connectedServer.equals("")) {
    		if (hasClient) {
    			
    			SburbConnection.connect(connectedServer,mc.thePlayer.username);
    			
    			Packet250CustomPayload packet = new Packet250CustomPayload();
    			packet.channel = "Minestuck";
    			packet.data = MinestuckPacket.makePacket(Type.SBURB_CONNECT,connectedServer,mc.thePlayer.username);
    			packet.length = packet.data.length;
    			mc.getNetHandler().addToSendQueue(packet);
        			
        		} else {
        			
        		}
    			
    		} else if (connectedClient.equals("") && !connectedServer.equals("")) {
    	  		if (hasServer) {
    				
        			Packet250CustomPayload packet = new Packet250CustomPayload();
        			packet.channel = "Minestuck";
        			packet.data = MinestuckPacket.makePacket(Type.SBURB_OPEN,connectedServer);
        			packet.length = packet.data.length;
        			mc.getNetHandler().addToSendQueue(packet);
    	  		} else {
    	  			
    	  		}
    		}
        	
    	if(gui != null)
    		gui.updateGui();
    	}

	@Override
	public void onConnected(String server, String client) {
		if (server == connectedServer && hasServer) {
				this.connectedClient = client;
				this.connectedServer = server;
				updateConnection();
	
				if (gui != null) {
					gui.updateGui();
				}
			} else if (owner == client && hasClient) {
				this.connectedClient = server;
				this.connectedServer = client;
		}
	}

	@Override
	public void onServerOpen(String server) {
		if (server == this.owner && hasServer) {
			this.connectedServer = server;
			if (gui != null) {
				gui.updateGui();
			}
		}
	}
    
}
