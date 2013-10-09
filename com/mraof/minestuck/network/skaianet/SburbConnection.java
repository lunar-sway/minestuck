package com.mraof.minestuck.network.skaianet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IConnectionListener;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class SburbConnection {
	
	public static SburbConnection load(DataInputStream stream) throws IOException{
		SburbConnection c = new SburbConnection();
		c.isActive = stream.readBoolean();
		
		if(c.isActive){
			c.client = ComputerData.load(stream);
			c.server = ComputerData.load(stream);
			c.isMain = stream.readBoolean();
			if(c.isMain)
				c.enteredGame = stream.readBoolean();
		}
		else{
			BufferedReader d = new BufferedReader(new InputStreamReader(stream));	//This to avoid the deprecated method stream.readLine()
			c.isMain = true;
			c.clientName = d.readLine();
			c.serverName = d.readLine();
			c.enteredGame = stream.readBoolean();
		}
		return c;
	}
	
//	@SideOnly(Side.SERVER)
	ComputerData client;
	String clientName = "";
//	@SideOnly(Side.SERVER)
	ComputerData server;
	String serverName = "";
	boolean isActive;
	boolean isMain;
	boolean enteredGame;
	
	SburbConnection(){
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
	
	void read(NBTTagCompound nbt) {
		isMain = nbt.getBoolean("isMain");
		if(isMain){
			isActive = nbt.getBoolean("isActive");
			enteredGame = nbt.getBoolean("enteredGame");
		}
		if(isActive){
			client = new ComputerData();
			server = new ComputerData();
			client.read(nbt.getCompoundTag("client"));
			server.read(nbt.getCompoundTag("server"));
		} else {
			clientName = nbt.getString("client");
			serverName = nbt.getString("server");
		}
	}
	
}
