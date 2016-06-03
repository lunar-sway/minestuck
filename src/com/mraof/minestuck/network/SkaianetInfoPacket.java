package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;

public class SkaianetInfoPacket extends MinestuckPacket
{
	
	public int playerId;
	public boolean isClientResuming, isServerResuming;
	public HashMap<Integer, String> openServers;
	public ArrayList<SburbConnection> connections;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat)
	{
		
		data.writeInt((Integer)dat[0]);
		
		if(dat.length == 1)	//If request from client
			return this;
		
		data.writeBoolean((Boolean)dat[1]);
		data.writeBoolean((Boolean)dat[2]);
		
		int size = (Integer)dat[3];
		data.writeInt(size);
		for(int i = 0; i < size; i++)
		{
			data.writeInt((Integer)dat[i*2+4]);
			writeString(data,((String)dat[i*2+5]+'\n'));
		}
		
		for(int i = size*2+4; i < dat.length; i++)
			((SburbConnection)dat[i]).writeBytes(data);
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		
		this.playerId = data.readInt();
		if(data.readableBytes() == 0)
			return this;
		isClientResuming = data.readBoolean();
		isServerResuming = data.readBoolean();
		int size = data.readInt();
		openServers = new HashMap<Integer, String>();
		for(int i = 0; i < size; i++)
			openServers.put(data.readInt(), readLine(data));
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
	public void execute(EntityPlayer player)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			SkaiaClient.consumePacket(this);
		else SkaianetHandler.requestInfo(player, IdentifierHandler.getById(this.playerId));
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.allOf(Side.class);
	}

}
