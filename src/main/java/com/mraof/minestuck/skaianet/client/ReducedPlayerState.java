package com.mraof.minestuck.skaianet.client;

import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;
import java.util.Map;

public record ReducedPlayerState(boolean isClientResuming, boolean isServerResuming,
								 boolean hasPrimaryConnectionAsClient, boolean hasPrimaryConnectionAsServer,
								 Map<Integer, String> openServers)
{
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(isClientResuming);
		buffer.writeBoolean(isServerResuming);
		
		buffer.writeBoolean(hasPrimaryConnectionAsClient);
		buffer.writeBoolean(hasPrimaryConnectionAsServer);
		
		buffer.writeInt(openServers.size());
		for(Map.Entry<Integer, String> entry : openServers.entrySet())
		{
			buffer.writeInt(entry.getKey());
			buffer.writeUtf(entry.getValue(), 16);
		}
	}
	
	public static ReducedPlayerState read(FriendlyByteBuf buffer)
	{
		boolean isClientResuming = buffer.readBoolean();
		boolean isServerResuming = buffer.readBoolean();
		
		boolean hasPrimaryConnectionAsClient = buffer.readBoolean();
		boolean hasPrimaryConnectionAsServer = buffer.readBoolean();
		
		int size = buffer.readInt();
		Map<Integer, String> openServers = new HashMap<>();
		for(int i = 0; i < size; i++)
			openServers.put(buffer.readInt(), buffer.readUtf(16));
		
		return new ReducedPlayerState(isClientResuming, isServerResuming,
				hasPrimaryConnectionAsClient, hasPrimaryConnectionAsServer, openServers);
	}
}
