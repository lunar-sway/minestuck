package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.SkaianetData;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.ReducedPlayerState;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public final class SkaianetInfoPacket
{
	public record Data(int playerId, ReducedPlayerState playerState, List<ReducedConnection> connections) implements MSPacket.PlayToClient
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeInt(playerId);
			
			playerState.write(buffer);
			
			buffer.writeCollection(connections, (buffer1, connection) -> connection.write(buffer1));
		}
		
		public static Data decode(FriendlyByteBuf buffer)
		{
			int playerId = buffer.readInt();
			
			ReducedPlayerState playerState = ReducedPlayerState.read(buffer);
			
			List<ReducedConnection> connections = buffer.readList(ReducedConnection::read);
			
			return new Data(playerId, playerState, connections);
		}
		
		@Override
		public void execute()
		{
			SkaiaClient.handlePacket(this);
		}
	}
	
	public record HasEntered(boolean hasEntered) implements MSPacket.PlayToClient
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(hasEntered);
		}
		
		public static HasEntered decode(FriendlyByteBuf buffer)
		{
			return new HasEntered(buffer.readBoolean());
		}
		
		@Override
		public void execute()
		{
			SkaiaClient.handlePacket(this);
		}
	}
	
	public record Request(int playerId) implements MSPacket.PlayToServer
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.playerId);
		}
		
		public static Request decode(FriendlyByteBuf buffer)
		{
			return new Request(buffer.readInt());
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			SkaianetData.get(player.server).requestInfo(player, IdentifierHandler.getById(this.playerId));
		}
	}
	
	public record LandChains(List<List<ResourceKey<Level>>> landChains) implements MSPacket.PlayToClient
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