package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import com.mraof.minestuck.skaianet.LandChain;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.ReducedPlayerState;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public final class SkaianetInfoPackets
{
	public record Data(int playerId, ReducedPlayerState playerState, List<ReducedConnection> connections) implements MSPacket.PlayToClient
	{
		
		public static final Type<Data> ID = new Type<>(Minestuck.id("skaianet_info/data"));
		public static final StreamCodec<FriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT,
				Data::playerId,
				ReducedPlayerState.STREAM_CODEC,
				Data::playerState,
				ReducedConnection.STREAM_CODEC.apply(ByteBufCodecs.list()),
				Data::connections,
				Data::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		
		@Override
		public void execute(IPayloadContext context)
		{
			SkaiaClient.handlePacket(this);
		}
	}
	
	public record HasEntered(boolean hasEntered) implements MSPacket.PlayToClient
	{
		public static final Type<HasEntered> ID = new Type<>(Minestuck.id("skaianet_info/has_entered"));
		public static final StreamCodec<ByteBuf, HasEntered> STREAM_CODEC = ByteBufCodecs.BOOL.map(HasEntered::new, HasEntered::hasEntered);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			SkaiaClient.handlePacket(this);
		}
	}
	
	public record Request(int playerId) implements MSPacket.PlayToServer
	{
		public static final Type<Request> ID = new Type<>(Minestuck.id("skaianet_info/request"));
		public static final StreamCodec<ByteBuf, Request> STREAM_CODEC = ByteBufCodecs.INT.map(Request::new, Request::playerId);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			ComputerInteractions.requestInfo(player, IdentifierHandler.getById(this.playerId));
		}
	}
	
	public record LandChains(List<LandChain> landChains) implements MSPacket.PlayToClient
	{
		public static final Type<LandChains> ID = new Type<>(Minestuck.id("skaianet_info/land_chains"));
		public static final StreamCodec<RegistryFriendlyByteBuf, LandChains> STREAM_CODEC = LandChain.STREAM_CODEC.apply(ByteBufCodecs.list()).map(LandChains::new, LandChains::landChains);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			SkaiaClient.handlePacket(this);
		}
	}
}
