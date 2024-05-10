package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record GristCachePacket(ImmutableGristSet gristCache, ClientPlayerData.CacheSource cacheSource) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("grist_cache");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		GristSet.write(gristCache, buffer);
		buffer.writeEnum(cacheSource);
	}
	
	public static GristCachePacket read(FriendlyByteBuf buffer)
	{
		ImmutableGristSet gristCache = GristSet.read(buffer);
		ClientPlayerData.CacheSource cacheSource = buffer.readEnum(ClientPlayerData.CacheSource.class);
		return new GristCachePacket(gristCache, cacheSource);
	}
	
	@Override
	public void execute()
	{
		ClientPlayerData.handleDataPacket(this);
	}
}
