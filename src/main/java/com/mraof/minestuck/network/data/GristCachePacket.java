package com.mraof.minestuck.network.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record GristCachePacket(ImmutableGristSet gristCache, boolean isEditmode) implements MSPacket.PlayToClient
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
		buffer.writeBoolean(isEditmode);
	}
	
	public static GristCachePacket read(FriendlyByteBuf buffer)
	{
		ImmutableGristSet gristCache = GristSet.read(buffer);
		boolean isEditmode = buffer.readBoolean();
		return new GristCachePacket(gristCache, isEditmode);
	}
	
	@Override
	public void execute()
	{
		ClientPlayerData.handleDataPacket(this);
	}
}