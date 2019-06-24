package com.mraof.minestuck.network;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SkaianetInfoPacket
{
	public int playerId;
	public boolean isClientResuming, isServerResuming;
	public Map<Integer, String> openServers;
	public List<SburbConnection> connections;
	public List<List<Integer>> landChains;
	
	public static SkaianetInfoPacket landChains(List<List<Integer>> landChains)
	{
		SkaianetInfoPacket packet = new SkaianetInfoPacket();
		packet.landChains = landChains;
		
		return packet;
	}
	
	public static SkaianetInfoPacket update(int playerId, boolean isClientResuming, boolean isServerResuming, Map<Integer, String> openServers, List<SburbConnection> connections)
	{
		SkaianetInfoPacket packet = new SkaianetInfoPacket();
		packet.playerId = playerId;
		packet.isClientResuming = isClientResuming;
		packet.isServerResuming = isServerResuming;
		packet.openServers = openServers;
		packet.connections = connections;
		
		return packet;
	}
	
	public static SkaianetInfoPacket request(int playerId)
	{
		SkaianetInfoPacket packet = new SkaianetInfoPacket();
		packet.playerId = playerId;
		
		return packet;
	}
	
	public void encode(PacketBuffer buffer)
	{
		if(landChains != null) //Land chain data
		{
			buffer.writeBoolean(true);
			for(List<Integer> list : landChains)
			{
				buffer.writeInt(list.size());
				for(int i : list)
					buffer.writeInt(i);
			}
		} else
		{
			buffer.writeBoolean(false);
			buffer.writeInt(playerId);
			
			if(connections != null)
			{
				
				buffer.writeBoolean(isClientResuming);
				buffer.writeBoolean(isServerResuming);
				
				buffer.writeInt(openServers.size());
				for(Map.Entry<Integer, String> entry : openServers.entrySet())
				{
					buffer.writeInt(entry.getKey());
					buffer.writeString(entry.getValue(), 16);
				}
				
				for(SburbConnection connection : connections)
					connection.toBuffer(buffer);
			}
		}
	}
	
	public static SkaianetInfoPacket decode(PacketBuffer buffer)
	{
		SkaianetInfoPacket packet = new SkaianetInfoPacket();
		if(buffer.readBoolean())
		{
			packet.landChains = new ArrayList<>();
			while(buffer.readableBytes() > 0)
			{
				int size = buffer.readInt();
				List<Integer> list = new ArrayList<>();
				for(int k = 0; k < size; k++)
					list.add(buffer.readInt());
				packet.landChains.add(list);
			}
		} else
		{
			packet.playerId = buffer.readInt();
			
			if(buffer.readableBytes() > 0)
			{
				packet.isClientResuming = buffer.readBoolean();
				packet.isServerResuming = buffer.readBoolean();
				int size = buffer.readInt();
				packet.openServers = new HashMap<>();
				for(int i = 0; i < size; i++)
					packet.openServers.put(buffer.readInt(), buffer.readString(16));
				packet.connections = new ArrayList<>();
				while(buffer.readableBytes() > 0)
					packet.connections.add(SkaiaClient.getConnectionFromBuffer(buffer));
			}
		}
		
		return packet;
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		else if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(this::execute);
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute()
	{
		SkaiaClient.consumePacket(this);
	}
	
	public void execute(EntityPlayerMP player)
	{
		SkaianetHandler.get(player.world).requestInfo(player, IdentifierHandler.getById(this.playerId));
	}
}