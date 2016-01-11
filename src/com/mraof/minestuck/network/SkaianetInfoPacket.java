package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;

public class SkaianetInfoPacket extends MinestuckPacket
{
	
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
		
		for(int i = size+4; i < dat.length; i++)
			((SburbConnection)dat[i]).writeBytes(data);
		
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
		try
		{
			while(data.readableBytes() > 0)
				connections.add(SkaiaClient.getConnection(data));
		} catch(IllegalStateException e)
		{
			e.printStackTrace();
		}
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player) {
		
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			SkaiaClient.consumePacket(this);
		else SkaianetHandler.requestInfo(player.getName(), this.player);
		
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.allOf(Side.class);
	}

}
