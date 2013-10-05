package com.mraof.minestuck.network;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.util.ArrayList;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaiaClient;
import com.mraof.minestuck.skaianet.SkaianetHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.Player;

public class SkaianetInfoPacket extends MinestuckPacket {

	public SkaianetInfoPacket() {
		super(Type.SBURB_INFO);
	}
	
	public String player;
	public boolean isClientResuming, isServerResuming;
	public ArrayList<String> openServers;
	
	@Override
	public byte[] generatePacket(Object... data) {
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.write(((String)data[0]+'\n').getBytes());	//Player name
		if(data.length == 1)	//If request from client
			return dat.toByteArray();
		dat.writeBoolean((Boolean)data[1]);
		dat.writeBoolean((Boolean)data[2]);
		
		int size = (Integer)data[3];
		dat.writeInt(size);
		for(int i = 0; i < size; i++)
			dat.write(((String)data[i+4]+'\n').getBytes());
		
		for(int i = size+3; i < data.length; i++){
			dat.write(((SburbConnection)data[i]).getBytes());
		}
		
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		
		this.player = dat.readLine();
		try{
			dat.readBoolean();
			dat.readBoolean();
			int size = dat.readInt();
			openServers = new ArrayList();
			for(int i = 0; i < size; i++)
				openServers.add(dat.readLine());
			
		} catch(IllegalStateException e){}	//Because I don't see a dat.available(); method or anything similar.
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName) {
		
		if(((EntityPlayer)player).worldObj.isRemote)
			SkaiaClient.consumePacket(this);
		else SkaianetHandler.requestInfo(((EntityPlayer)player).username, this.player);
		
	}

}
