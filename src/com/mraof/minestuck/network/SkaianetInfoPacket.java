package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.relauncher.Side;

public class SkaianetInfoPacket extends MinestuckPacket {

	public SkaianetInfoPacket() {
		super(Type.SBURB_INFO);
	}
	
	public String player;
	public boolean isClientResuming, isServerResuming;
	public ArrayList<String> openServers;
	public ArrayList<SburbConnection> connections;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat) {
		
		writeString(data, dat[0].toString()+'\n');
		
		if(dat.length == 1) {	//If request from client
			return this;
		}
		data.writeBoolean((Boolean)dat[1]);
		data.writeBoolean((Boolean)dat[2]);
		
		int size = (Integer)dat[3];
		data.writeInt(size);
		for(int i = 0; i < size; i++)
			writeString(data,((String)dat[i+4]+'\n'));
		
		for(int i = size+4; i < dat.length; i++){
			data.writeBytes(((SburbConnection)dat[i]).getBytes());
		}
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) {
		
		this.player = readLine(data);
		if(data.readableBytes() == 0)
			return this;
		isClientResuming = data.readBoolean();
		isServerResuming = data.readBoolean();
		int size = data.readInt();
		openServers = new ArrayList<String>();
		for(int i = 0; i < size; i++)
			openServers.add(readLine(data));
		connections = new ArrayList<SburbConnection>();
		byte[] b = new byte[data.readableBytes()];
		data.readBytes(b);
		ByteArrayDataInput dat = ByteStreams.newDataInput(b);
		try{
			while(true)
				connections.add(SkaiaClient.getConnection(dat));	//TODO change parameter of this method.
		} catch(IllegalStateException e){}
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player) {
		
		if(((EntityPlayer)player).worldObj.isRemote)
			SkaiaClient.consumePacket(this);
		else SkaianetHandler.requestInfo(((EntityPlayer)player).getCommandSenderName(), this.player);
		
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.allOf(Side.class);
	}

}
