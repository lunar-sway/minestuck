package com.mraof.minestuck.network.skaianet;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.nbt.NBTTagCompound;

public class SburbConnection {
	
	
//	@SideOnly(Side.SERVER)
	ComputerData client;
	String clientName = "";
//	@SideOnly(Side.SERVER)
	ComputerData server;
	String serverName = "";
	boolean isActive;
	boolean isMain;
	boolean enteredGame;
	boolean canSplit;
	
	SburbConnection(){
		this.canSplit = true;
		this.isActive = true;
	}
	
	public String getClientName(){
		if(clientName.isEmpty())
			return client.owner;
		else return clientName;
	}
	public String getServerName(){
		if(serverName.isEmpty())
			return server.owner;
		else return serverName;
	}
	public boolean givenItems(){return isMain;}
	public boolean enteredGame(){return enteredGame;}

	public byte[] getBytes() {
		ByteArrayDataOutput data = ByteStreams.newDataOutput();
		
		data.writeBoolean(isMain);
		if(isMain){
			data.writeBoolean(isActive);
			data.writeBoolean(enteredGame);
		}
		data.write((getClientName()+"\n").getBytes());
		data.write((getServerName()+"\n").getBytes());
		
		return data.toByteArray();
	}

	NBTTagCompound write() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("isMain", isMain);
		if(isMain){
			nbt.setBoolean("isActive", isActive);
			nbt.setBoolean("enteredGame", enteredGame);
			nbt.setBoolean("canSplit", canSplit);
		}
		if(isActive){
			nbt.setCompoundTag("client", client.write());
			nbt.setCompoundTag("server", server.write());
		} else {
			nbt.setString("client", clientName);
			nbt.setString("server", serverName);
		}
		return nbt;
	}
	
	SburbConnection read(NBTTagCompound nbt) {
		isMain = nbt.getBoolean("isMain");
		if(isMain){
			isActive = nbt.getBoolean("isActive");
			enteredGame = nbt.getBoolean("enteredGame");
			if(nbt.hasKey("canSplit"))
				canSplit = nbt.getBoolean("canSplit");
		}
		if(isActive){
			client = new ComputerData().read(nbt.getCompoundTag("client"));
			server = new ComputerData().read(nbt.getCompoundTag("server"));
		} else {
			clientName = nbt.getString("client");
			serverName = nbt.getString("server");
		}
		return this;
	}
	
	@Override
		public boolean equals(Object obj) {
		if(obj instanceof SburbConnection){
			SburbConnection c = (SburbConnection)obj;
			return this.getClientName() == c.getClientName() && this.getServerName() == c.getServerName();
		}
		return false;
	}
}
