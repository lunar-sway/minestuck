package com.mraof.minestuck.tileentity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IConnectionListener;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityComputer extends TileEntity implements IConnectionListener {
	
	//public volatile boolean hasClient = false;
	//public volatile boolean hasServer = false;
	public Hashtable installedPrograms = new Hashtable();
	public boolean openToClients = false;
	public SburbConnection client;
	/**
	 * To not be confused, serverConnected = if it has a server connected to it (so serverConnected can only be true if hasClient == true)
	 */
	public boolean serverConnected;
	public String clientName = "";
	public SburbConnection server;
	public GuiComputer gui;
	public String owner = "";
	public String latestmessage = "";
	public boolean resumingClient;
	/**
	 * 0 if client is selected, 1 if server. (client side varable)
	 */
	public int programSelected = -1;
	
    public TileEntityComputer() {
            SburbConnection.addListener(this);
    }
    
    @Override
    public void updateEntity() {if(MinecraftServer.getServer() != null) Debug.print(MinecraftServer.getServer().worldServers.length);
    	if(server == null && serverConnected){
    		server = SburbConnection.getClientConnection(owner);
    		if(gui != null)
    			gui.updateGui();
    	}
    	if(client == null && !clientName.isEmpty()){
    		client = SburbConnection.getClientConnection(clientName);
    		if(gui != null)
    			gui.updateGui();
    	}
    }
    
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);	
		if (par1NBTTagCompound.getCompoundTag("programs") != null) {
			for (Object tag : par1NBTTagCompound.getCompoundTag("programs").getTags()) {
				installedPrograms.put(((NBTTagInt)tag).data,true);
			}
		}
		this.clientName = par1NBTTagCompound.getString("connectClient");
		this.serverConnected = par1NBTTagCompound.getBoolean("connectServer");
		this.openToClients = par1NBTTagCompound.getBoolean("serverOpen");
		this.resumingClient = par1NBTTagCompound.getBoolean("resumeClient");
    	 this.owner = par1NBTTagCompound.getString("owner");
    	if(!this.clientName.isEmpty() && client == null)
    		client = SburbConnection.getClientConnection(clientName);
    	if(this.serverConnected && server == null)
    		server = SburbConnection.getClientConnection(owner);
    	 if(gui != null)
    		 gui.updateGui();
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    	super.writeToNBT(par1NBTTagCompound);
    	NBTTagCompound programs = new NBTTagCompound();
   	   	Iterator it = this.installedPrograms.entrySet().iterator();
	   	int place = 0;
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        int program = (Integer) pairs.getKey();
	        programs.setInteger("program"+program,program);
	        place++;
         }
    	par1NBTTagCompound.setCompoundTag("programs",programs);
    	par1NBTTagCompound.setString("connectClient",this.clientName);
    	par1NBTTagCompound.setBoolean("connectServer", this.serverConnected);
    	par1NBTTagCompound.setBoolean("serverOpen", this.openToClients);
    	par1NBTTagCompound.setBoolean("resumeClient",this.resumingClient);
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
    	this.readFromNBT(pkt.data);
    }
    
    @SideOnly(Side.CLIENT)
    public void resume(boolean isClient){
    	Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.SBURB_RESUME,owner,xCoord,yCoord,zCoord,worldObj.provider.dimensionId,isClient);
		packet.length = packet.data.length;
		Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
    }
    
    @SideOnly(Side.CLIENT)
	public void openServer(){
		if(hasServer() && !openToClients && client == null){
			openToClients = true;
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_OPEN,owner,xCoord,yCoord,zCoord,worldObj.provider.dimensionId);
			packet.length = packet.data.length;
			Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
		}
	}
	
    @SideOnly(Side.CLIENT)
	public void connectToServer(String server){
		if(hasClient() && this.server == null){
			this.serverConnected = true;
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_CONNECT,owner,xCoord,yCoord,zCoord,worldObj.provider.dimensionId,server);
			packet.length = packet.data.length;
			Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
		}
	}
	
    @SideOnly(Side.CLIENT)
	public void giveItems(){
		if(hasServer() && client != null && !client.givenItems()){
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_GIVE,client.getClientName());
			packet.length = packet.data.length;
			Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
		}
	}
	
	public void closeConnection(boolean client, boolean server) { //Can be called when disconnecting through GUI or on destroying the block.
		if(server || client){
			if(server && client)
				SburbConnection.removeListener(this);
			if(this.hasClient() && (this.server != null || this.resumingClient) && client){
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "Minestuck";
				if(resumingClient)
					packet.data = MinestuckPacket.makePacket(Type.SBURB_CLOSE,owner,"");
				else packet.data = MinestuckPacket.makePacket(Type.SBURB_CLOSE,owner,this.server.getServerName());
				packet.length = packet.data.length;
				if(!worldObj.isRemote){
					if(resumingClient)
						SburbConnection.connectionClosed(owner,"");
					else SburbConnection.connectionClosed(owner,this.server.getServerName());
					PacketDispatcher.sendPacketToAllPlayers(packet);
				}
				else Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
			}
			if(this.hasServer() && server && (openToClients || this.client != null)){
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "Minestuck";
				if(openToClients)
					packet.data = MinestuckPacket.makePacket(Type.SBURB_CLOSE,"",owner);
				else packet.data = MinestuckPacket.makePacket(Type.SBURB_CLOSE,this.client.getClientName(),owner);
				packet.length = packet.data.length;
				if(!worldObj.isRemote){
					if(openToClients)
						SburbConnection.connectionClosed("",owner);
					else SburbConnection.connectionClosed(this.client.getClientName(),owner);
					PacketDispatcher.sendPacketToAllPlayers(packet);
				}
				else Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
			}
		}
	}
	
	public void onConnectionClosed(String client, String server){
		if(this.hasClient() && client.equals(this.owner) && this.server != null && server.equals(this.server.getServerName())){
			if(programSelected == 0)
				latestmessage = "Connection with server closed";
			this.server = null;
			serverConnected = false;
		}
		else if(this.hasClient() && client.equals(this.owner) && this.resumingClient && server.isEmpty()){
			latestmessage = "Stopped resuming";
			this.resumingClient = false;
		}
		if(this.hasServer() && server.equals(this.owner) && this.client != null && client.equals(this.client.getClientName())){
			if(programSelected == 1)
				latestmessage = "Connection with client closed";
			this.client = null;
			clientName = "";
		}
		else if(this.hasServer() && server.equals(this.owner) && openToClients && client.isEmpty()){
			latestmessage = "Server closed";
			this.openToClients = false;
			
		}
		
		if(gui != null)
			gui.updateGui();
	}
	
	@Override
	public void onConnected(String client, String server) {
		if(owner.equals(server) && hasServer() && this.client == null && openToClients){
			openToClients = false;
			clientName = client;
		}
		if(owner.equals(client) && hasClient() && resumingClient){
			resumingClient = false;
			serverConnected = true;
		}
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
	
	public Boolean hasClient() {
		return (Boolean)installedPrograms.get(0)==null?false:(Boolean)installedPrograms.get(0);
	}
	
	public Boolean hasServer() {
		return (Boolean)installedPrograms.get(1)==null?false:(Boolean)installedPrograms.get(1);
	}
	
	public void connected(SburbConnection c, boolean isClient){
		if(isClient){
			this.server = c;
			this.serverConnected = true;
		}
		else{
			this.client = c;
			this.clientName = c.getClientName();
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
}
