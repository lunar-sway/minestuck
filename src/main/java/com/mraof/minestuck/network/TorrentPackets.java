package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.TorrentSession;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class TorrentPackets
{
	public record UpdateClient(Map<TorrentSession, TorrentSession.LimitedCache> data) implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("torrent_update_client");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeJsonWithCodec(TorrentSession.TORRENT_DATA_CODEC, data);
		}
		
		public static UpdateClient read(FriendlyByteBuf buffer)
		{
			Map<TorrentSession, TorrentSession.LimitedCache> data = buffer.readJsonWithCodec(TorrentSession.TORRENT_DATA_CODEC);
			
			return new UpdateClient(data);
		}
		
		@Override
		public void execute()
		{
			ClientPlayerData.handleDataPacket(this);
		}
	}
	
	public record ModifySeeding(GristType gristType, boolean isSeeding) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("torrent_modify_seeding");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeId(GristTypes.REGISTRY, gristType);
			buffer.writeBoolean(isSeeding);
		}
		
		public static ModifySeeding read(FriendlyByteBuf buffer)
		{
			GristType gristType = buffer.readById(GristTypes.REGISTRY);
			boolean isSeeding  = buffer.readBoolean();
			
			return new ModifySeeding(gristType, isSeeding);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			MinecraftServer server = player.server;
			MSExtraData data = MSExtraData.get(server);
			
			data.updateTorrentSeeding((IdentifierHandler.UUIDIdentifier) IdentifierHandler.encode(player), gristType, isSeeding);
		}
	}
	
	public record ModifyLeeching(TorrentSession torrentSession, GristType gristType, boolean isLeeching) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("torrent_modify_leeching");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeJsonWithCodec(TorrentSession.CODEC, torrentSession);
			buffer.writeId(GristTypes.REGISTRY, gristType);
			buffer.writeBoolean(isLeeching);
		}
		
		public static ModifyLeeching read(FriendlyByteBuf buffer)
		{
			TorrentSession torrentSession = buffer.readJsonWithCodec(TorrentSession.CODEC);
			GristType gristType = buffer.readById(GristTypes.REGISTRY);
			boolean isLeeching  = buffer.readBoolean();
			
			return new ModifyLeeching(torrentSession, gristType, isLeeching);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			MinecraftServer server = player.server;
			MSExtraData data = MSExtraData.get(server);
			
			data.updateTorrentLeeching(torrentSession, (IdentifierHandler.UUIDIdentifier) IdentifierHandler.encode(player), gristType, isLeeching);
		}
	}
}