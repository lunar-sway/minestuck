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

import java.util.List;

public class TorrentPackets
{
	public record UpdateClient(List<TorrentSession> availableSessions) implements MSPacket.PlayToClient
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
			buffer.writeJsonWithCodec(TorrentSession.LIST_CODEC, availableSessions);
		}
		
		public static UpdateClient read(FriendlyByteBuf buffer)
		{
			List<TorrentSession> availableSessions = buffer.readJsonWithCodec(TorrentSession.LIST_CODEC);
			
			return new UpdateClient(availableSessions);
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
}