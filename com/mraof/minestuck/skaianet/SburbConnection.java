package com.mraof.minestuck.skaianet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

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
	
}
