package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.*;

public class SkaianetInfoPacket implements MSPacket.PlayToBoth
{
	public int playerId;
	public boolean isClientResuming, isServerResuming;
	public Map<Integer, String> openServers;
	public List<ReducedConnection> connections;
	
	public static SkaianetInfoPacket update(int playerId, boolean isClientResuming, boolean isServerResuming, Map<Integer, String> openServers, List<ReducedConnection> connections)
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
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(playerId);
		
		if(connections != null)
		{
			
			buffer.writeBoolean(isClientResuming);
			buffer.writeBoolean(isServerResuming);
			
			buffer.writeInt(openServers.size());
			for(Map.Entry<Integer, String> entry : openServers.entrySet())
			{
				buffer.writeInt(entry.getKey());
				buffer.writeUtf(entry.getValue(), 16);
			}
			
			for(ReducedConnection connection : connections)
				connection.write(buffer);
		}
	}
	
	public static SkaianetInfoPacket decode(FriendlyByteBuf buffer)
	{
		SkaianetInfoPacket packet = new SkaianetInfoPacket();
		packet.playerId = buffer.readInt();
		
		if(buffer.readableBytes() > 0)
		{
			packet.isClientResuming = buffer.readBoolean();
			packet.isServerResuming = buffer.readBoolean();
			int size = buffer.readInt();
			packet.openServers = new HashMap<>();
			for(int i = 0; i < size; i++)
				packet.openServers.put(buffer.readInt(), buffer.readUtf(16));
			packet.connections = new ArrayList<>();
			while(buffer.readableBytes() > 0)
				packet.connections.add(ReducedConnection.read(buffer));
		}
		
		return packet;
	}
	
	public void execute()
	{
		SkaiaClient.consumePacket(this);
	}
	
	public void execute(ServerPlayer player)
	{
		SkaianetHandler.get(player.server).requestInfo(player, IdentifierHandler.getById(this.playerId));
	}
	
	public record LandChains(List<List<ResourceKey<Level>>> landChains) implements PlayToClient
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeCollection(landChains, (buffer1, list) ->
					buffer1.writeCollection(list, (buffer2, land) ->
							buffer2.writeOptional(Optional.ofNullable(land),
									FriendlyByteBuf::writeResourceKey)));
		}
		
		public static LandChains decode(FriendlyByteBuf buffer)
		{
			List<List<ResourceKey<Level>>> landChains = buffer.readList(buffer1 ->
					buffer1.readList(buffer2 ->
							buffer2.readOptional(buffer3 ->
									buffer3.readResourceKey(Registries.DIMENSION)
							).orElse(null)));
			
			return new LandChains(landChains);
		}
		
		@Override
		public void execute()
		{
			SkaiaClient.handlePacket(this);
		}
	}
}