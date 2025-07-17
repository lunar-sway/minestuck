package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.TorrentSession;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public class TorrentPackets
{
	public record UpdateClient(Map<Integer, TorrentSession.TorrentClientData> data) implements MSPacket.PlayToClient
	{
		public static final Type<UpdateClient> ID = new Type<>(Minestuck.id("torrent_update_client"));
		private static final StreamCodec<RegistryFriendlyByteBuf, Map<Integer, TorrentSession.TorrentClientData>> MAP_STREAM_CODEC = ByteBufCodecs.map(HashMap::new, ByteBufCodecs.INT, TorrentSession.TorrentClientData.STREAM_CODEC);
		public static final StreamCodec<RegistryFriendlyByteBuf, UpdateClient> STREAM_CODEC = MAP_STREAM_CODEC.map(UpdateClient::new, UpdateClient::data);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			ClientPlayerData.handleDataPacket(this);
		}
	}
	
	public record ModifySeeding(GristType gristType, boolean isSeeding) implements MSPacket.PlayToServer
	{
		public static final Type<ModifySeeding> ID = new Type<>(Minestuck.id("torrent_modify_seeding"));
		public static final StreamCodec<RegistryFriendlyByteBuf, ModifySeeding> STREAM_CODEC = StreamCodec.composite(
				GristType.STREAM_CODEC,
				ModifySeeding::gristType,
				ByteBufCodecs.BOOL,
				ModifySeeding::isSeeding,
				ModifySeeding::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			MinecraftServer server = player.server;
			MSExtraData data = MSExtraData.get(server);
			
			data.updateTorrentSeeding(IdentifierHandler.encode(player), gristType, isSeeding);
		}
	}
	
	public record ModifyLeeching(Integer targetId, GristType gristType,
								 boolean isLeeching) implements MSPacket.PlayToServer
	{
		public static final Type<ModifyLeeching> ID = new Type<>(Minestuck.id("torrent_modify_leeching"));
		public static final StreamCodec<RegistryFriendlyByteBuf, ModifyLeeching> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT,
				ModifyLeeching::targetId,
				GristType.STREAM_CODEC,
				ModifyLeeching::gristType,
				ByteBufCodecs.BOOL,
				ModifyLeeching::isLeeching,
				ModifyLeeching::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			MinecraftServer server = player.server;
			MSExtraData data = MSExtraData.get(server);
			
			data.updateTorrentLeeching(IdentifierHandler.getById(targetId), IdentifierHandler.encode(player), gristType, isLeeching);
		}
	}
}
