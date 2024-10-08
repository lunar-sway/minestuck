package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import com.mraof.minestuck.skaianet.LandChain;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.ReducedPlayerState;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public final class SkaianetInfoPackets
{
	public record Data(int playerId, ReducedPlayerState playerState, List<ReducedConnection> connections) implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("skaianet_info/data");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeInt(playerId);
			
			playerState.write(buffer);
			
			buffer.writeCollection(connections, (buffer1, connection) -> connection.write(buffer1));
		}
		
		public static Data read(FriendlyByteBuf buffer)
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
		public static final ResourceLocation ID = Minestuck.id("skaianet_info/has_entered");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(hasEntered);
		}
		
		public static HasEntered read(FriendlyByteBuf buffer)
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
		public static final ResourceLocation ID = Minestuck.id("skaianet_info/request");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.playerId);
		}
		
		public static Request read(FriendlyByteBuf buffer)
		{
			return new Request(buffer.readInt());
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			ComputerInteractions.requestInfo(player, IdentifierHandler.getById(this.playerId));
		}
	}
	
	public record LandChains(List<LandChain> landChains) implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("skaianet_info/land_chains");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeCollection(landChains, (buffer1, landChain) -> landChain.write(buffer1));
		}
		
		public static LandChains read(FriendlyByteBuf buffer)
		{
			List<LandChain> landChains = buffer.readList(LandChain::read);
			
			return new LandChains(landChains);
		}
		
		@Override
		public void execute()
		{
			SkaiaClient.handlePacket(this);
		}
	}
}
