package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.TorrentSession;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record TorrentUpdatePacket(List<TorrentSession> availableSessions) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("torrent_update");
	
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
	
	public static TorrentUpdatePacket read(FriendlyByteBuf buffer)
	{
		List<TorrentSession> availableSessions = buffer.readJsonWithCodec(TorrentSession.LIST_CODEC);
		
		return new TorrentUpdatePacket(availableSessions);
	}
	
	@Override
	public void execute()
	{
		ClientPlayerData.handleDataPacket(this);
	}
}