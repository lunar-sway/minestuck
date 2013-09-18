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
	public String connectedClient = "";
	public boolean givenItems = false;
	public String connectedServer = "";
	public GuiComputer gui;
	public String owner = "";
	public String latestmessage = "";
	private Minecraft mc = Minecraft.getMinecraft();public int id;
	public int programSelected = -1;	//0 if client is selected, 1 if server.
	
    public TileEntityComputer() {id = new Random().nextInt(100);
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
		this.openToClients = par1NBTTagCompound.getBoolean("serverOpen");
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
    	par1NBTTagCompound.setBoolean("serverOpen", this.openToClients);
    	if (!this.connectedClient.isEmpty()) {
    		par1NBTTagCompound.setString("client",this.connectedClient);
    	}
    	if (!this.connectedServer.isEmpty()) {
    		par1NBTTagCompound.setString("server",this.connectedServer);
    	}
    	if (!this.owner.isEmpty()) {
    		par1NBTTagCompound.setString("owner",this.owner);
    	}

    }
//    public void updateEntity(){if(hasServer)Debug.print(openToClients+","+id+","+worldObj.isRemote);}
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
		if(hasServer && !openToClients && connectedClient.isEmpty()){
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_OPEN,xCoord,yCoord,zCoord);
			packet.length = packet.data.length;
			mc.getNetHandler().addToSendQueue(packet);
		}
	}
	
	public void connectToServer(String server){
		if(hasClient && connectedServer.isEmpty()){
			connectedServer = server;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_CONNECT,owner,connectedServer);
			packet.length = packet.data.length;
			mc.getNetHandler().addToSendQueue(packet);
		}
	}
	
	public void giveItems(){
		if(hasServer && !connectedClient.isEmpty() && !givenItems){
			givenItems = true;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_GIVE,connectedClient);
			packet.length = packet.data.length;
			this.mc.getNetHandler().addToSendQueue(packet);
		}
	}
	
	public void closeConnection(boolean client, boolean server) {
		if((server || client) && !worldObj.isRemote){Debug.print(connectedServer);
		if(server && client)
			SburbConnection.removeListener(this);
			if(this.hasClient && !connectedServer.isEmpty() && client){
				SburbConnection.connectionClosed(this.connectedServer, this.owner);
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "Minestuck";
				packet.data = MinestuckPacket.makePacket(Type.SBURB_CLOSE,owner,connectedServer);
				packet.length = packet.data.length;
				PacketDispatcher.sendPacketToAllPlayers(packet);
			}
			if(this.hasServer && server && (openToClients || !connectedClient.isEmpty())){
				SburbConnection.connectionClosed(this.owner, this.connectedClient);
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "Minestuck";
				packet.data = MinestuckPacket.makePacket(Type.SBURB_CLOSE,connectedClient,owner);
				packet.length = packet.data.length;
				PacketDispatcher.sendPacketToAllPlayers(packet);
			}
		}
	}
	
	public void onConnectionClosed(String client, String server){
		if(this.hasClient && client.equals(this.owner) && server.equals(this.connectedServer)){
			latestmessage = "Server disconnected";
			this.connectedServer = "";
		}
		else if(this.hasServer && server.equals(this.owner) && client.equals(this.connectedClient)){
			latestmessage = "Client disconnected";
			this.connectedClient = "";Debug.print("empty");
		}
		
		if(gui != null)
			gui.updateGui();
	}
	
	@Override
	public void onConnected(String client, String server) {
		if(!worldObj.isRemote){Debug.print("onConnected called:"+worldObj.isRemote+","+id+","+owner+","+client+","+server+","+","+hasClient+","+hasServer+","+openToClients+","+owner.equals(server) + hasServer + openToClients);
		if (owner.equals(server) && hasServer && openToClients) {
				this.connectedClient = client;Debug.print("client connected:"+connectedClient);
				openToClients = false;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		} else if (owner.equals(client) && hasClient && this.connectedServer.isEmpty()) {
			this.connectedServer = server;Debug.print("connected to server:"+connectedServer);
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		if (gui != null)
			gui.updateGui();
	}}

	@Override
	public void onServerOpen(String server) {
		if (gui != null) {
			gui.updateGui();
		}
	}
    
}
