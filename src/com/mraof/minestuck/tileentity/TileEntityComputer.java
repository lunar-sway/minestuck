package com.mraof.minestuck.tileentity;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;

public class TileEntityComputer extends TileEntity {
	
	/**
	 * 0 = client, 1 = server, -1 = secret easter egg
	 */
	public Hashtable<Integer, Boolean> installedPrograms = new Hashtable<Integer, Boolean>();
	public boolean openToClients = false;
	/**
	 * To not be confused, serverConnected = if it has a server connected to it (so serverConnected can only be true if hasClient == true)
	 */
	public boolean serverConnected;
	public String clientName = "";
	public GuiComputer gui;
	public String owner = "";
	public Hashtable<Integer,String> latestmessage = new Hashtable<Integer,String>();
	public boolean resumingClient;
	/**
	 * 0 if client is selected, 1 if server. (client side variable)
	 */
	public int programSelected = -1;
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);	
		if (par1NBTTagCompound.getCompoundTag("programs") != null) {
			NBTTagCompound programs = par1NBTTagCompound.getCompoundTag("programs");
			for (Object name : programs.func_150296_c()) {
				installedPrograms.put(programs.getInteger((String)name),true);
			}
		}
		if(hasClient())
			latestmessage.put(0, par1NBTTagCompound.getString("text0"));
		if(hasServer())
			latestmessage.put(1, par1NBTTagCompound.getString("text1"));
		
		this.clientName = par1NBTTagCompound.getString("connectClient");
		this.serverConnected = par1NBTTagCompound.getBoolean("connectServer");
		this.openToClients = par1NBTTagCompound.getBoolean("serverOpen");
		this.resumingClient = par1NBTTagCompound.getBoolean("resumeClient");
    	 this.owner = par1NBTTagCompound.getString("owner");
    	 if(gui != null)
    		 gui.updateGui();
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    	super.writeToNBT(par1NBTTagCompound);
    	NBTTagCompound programs = new NBTTagCompound();
		Iterator<Entry<Integer, Boolean>> it = this.installedPrograms.entrySet().iterator();
	   	//int place = 0;
	    while (it.hasNext()) {
			Map.Entry<Integer, Boolean> pairs = (Entry<Integer, Boolean>) it
					.next();
	        int program = (Integer) pairs.getKey();
	        programs.setInteger("program"+program,program);
	        //place++;
         }
	    for(Entry<Integer, String> e : latestmessage.entrySet())
	    	par1NBTTagCompound.setString("text"+e.getKey(), e.getValue());
    	par1NBTTagCompound.setTag("programs",programs);
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
    	return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 2, tagCompound);
    }
    
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
    {
		//Debug.print("Data packet gotten "+net.getClass());
    	this.readFromNBT(pkt.func_148857_g());
    }
    
	public Boolean hasClient() {
		return installedPrograms.get(0)==null?false:installedPrograms.get(0);
	}
	
	public Boolean hasServer() {
		return installedPrograms.get(1)==null?false:installedPrograms.get(1);
	}
	
	public Boolean errored() {
		return installedPrograms.get(-1)==null?false:installedPrograms.get(-1);
	}
	
	public void connected(String player, boolean isClient){
		if(isClient){
			this.resumingClient = false;
			this.serverConnected = true;
		}
		else{
			this.openToClients = false;
			this.clientName = player;
		}
	}

	public void closeConnections() {
		if(serverConnected && SkaianetHandler.getClientConnection(owner) != null)
			SkaianetHandler.closeConnection(owner, SkaianetHandler.getClientConnection(owner).getServerName(), true);
		else if(resumingClient)
			SkaianetHandler.closeConnection(owner, "", true);
		if(!clientName.isEmpty())
			SkaianetHandler.closeConnection(owner, clientName, false);
		else if(openToClients)
			SkaianetHandler.closeConnection(owner, "", false);
	}
	
}
