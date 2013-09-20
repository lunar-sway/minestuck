package com.mraof.minestuck.tileentity;

import java.util.Random;

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

import cpw.mods.fml.common.network.PacketDispatcher;

public class TileEntityComputer extends TileEntity implements IConnectionListener {
	
	public volatile boolean hasClient = false;
	public volatile boolean hasServer = false;
	public boolean openToClients = false;
	public SburbConnection client;
	public boolean serverConnected;	//To not be confused, serverConnected = if it has a server connected to it (so serverConnected can only be true if hasClient == true)
	public String clientName = "";
	public SburbConnection server;
	public GuiComputer gui;
	public String owner = "";
	public String latestmessage = "";
	private Minecraft mc = Minecraft.getMinecraft();
	public int programSelected = -1;	//0 if client is selected, 1 if server.
	
    public TileEntityComputer() {
            SburbConnection.addListener(this);
            if (Minecraft.getMinecraft().thePlayer != null) {
            	owner = Minecraft.getMinecraft().thePlayer.username;
            }
    }
    
    @Override
    public void updateEntity() {
    	if(server == null && serverConnected)
    		server = SburbConnection.getClientConnection(owner);
    	if(client == null && !clientName.isEmpty())
    		client = SburbConnection.getClientConnection(clientName);
    }
    
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.hasClient = par1NBTTagCompound.getBoolean("hasClient");
		this.hasServer = par1NBTTagCompound.getBoolean("hasServer");
		this.clientName = par1NBTTagCompound.getString("connectClient");
		this.serverConnected = par1NBTTagCompound.getBoolean("connectServer");
		this.openToClients = par1NBTTagCompound.getBoolean("serverOpen");
    	 this.owner = par1NBTTagCompound.getString("owner");
    	if(!this.clientName.isEmpty() && client == null)
    		client = SburbConnection.getClientConnection(clientName);
    	if(this.serverConnected && server == null)
    		server = SburbConnection.getClientConnection(owner);
    	if(openToClients && !SburbConnection.getServersOpen().contains(owner))
    		SburbConnection.openServer(owner, xCoord, yCoord, zCoord, worldObj.provider.dimensionId);
    	 if(gui != null)
    		 gui.updateGui();
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setBoolean("hasClient",this.hasClient);
    	par1NBTTagCompound.setBoolean("hasServer",this.hasServer);
    	par1NBTTagCompound.setString("connectClient",this.clientName);
    	par1NBTTagCompound.setBoolean("connectServer", this.serverConnected);
    	par1NBTTagCompound.setBoolean("serverOpen", this.openToClients);
    	if (!this.owner.isEmpty()) {
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
    
	public void openServer(){
		if(hasServer && !openToClients && client == null){
			openToClients = true;
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_OPEN,owner,xCoord,yCoord,zCoord,worldObj.provider.dimensionId);
			packet.length = packet.data.length;
			mc.getNetHandler().addToSendQueue(packet);
		}
	}
	
	public void connectToServer(String server){
		if(hasClient && this.server == null){
			this.serverConnected = true;
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_CONNECT,owner,xCoord,yCoord,zCoord,worldObj.provider.dimensionId,server);
			packet.length = packet.data.length;
			mc.getNetHandler().addToSendQueue(packet);
		}
	}
	
	public void giveItems(){
		if(hasServer && client != null && !client.givenItems()){
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_GIVE,client.getClient().getOwner());
			packet.length = packet.data.length;
			this.mc.getNetHandler().addToSendQueue(packet);
		}
	}
	
	public void closeConnection(boolean client, boolean server) {
		if((server || client) && !worldObj.isRemote){
			if(server && client)
				SburbConnection.removeListener(this);
			if(this.hasClient && this.server != null && client){
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "Minestuck";
				packet.data = MinestuckPacket.makePacket(Type.SBURB_CLOSE,owner,this.server.getServer().getOwner());
				packet.length = packet.data.length;
				if(worldObj.isRemote)
					PacketDispatcher.sendPacketToAllPlayers(packet);
				else this.mc.getNetHandler().addToSendQueue(packet);
			}
			if(this.hasServer && server && (openToClients || this.client != null)){
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "Minestuck";
				if(openToClients)
					packet.data = MinestuckPacket.makePacket(Type.SBURB_CLOSE,"",owner);
				else packet.data = MinestuckPacket.makePacket(Type.SBURB_CLOSE,this.client.getClient().getOwner(),owner);
				packet.length = packet.data.length;
				if(worldObj.isRemote)
					PacketDispatcher.sendPacketToAllPlayers(packet);
				else this.mc.getNetHandler().addToSendQueue(packet);
			}
		}
	}
	
	public void onConnectionClosed(String client, String server){
		if(this.hasClient && client.equals(this.owner) && this.server != null && server.equals(this.server.getServer().getOwner())){
			latestmessage = "Server disconnected";
			this.server = null;
		}
		else if(this.hasServer && server.equals(this.owner) && this.client != null && client.equals(this.client.getClient().getOwner())){
			latestmessage = "Client disconnected";
			this.client = null;
		}
		
		if(gui != null)
			gui.updateGui();
	}
	
	@Override
	public void onConnected(String client, String server) {
		if (gui != null)
			gui.updateGui();
	}

	@Override
	public void onServerOpen(String server) {
		if (gui != null) {
			gui.updateGui();
		}
	}
	
	@Override
	public void newPermaConnection(String client, String server) {
		if(gui != null) {
			gui.updateGui();
		}
		
	}
    
}
