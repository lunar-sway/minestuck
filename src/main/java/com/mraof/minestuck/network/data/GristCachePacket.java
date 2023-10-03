package com.mraof.minestuck.network.data;

import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.network.PlayToClientPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;

public record GristCachePacket(ImmutableGristSet gristCache, boolean isEditmode) implements PlayToClientPacket
{
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		GristSet.write(gristCache, buffer);
		buffer.writeBoolean(isEditmode);
	}
	
	public static GristCachePacket decode(FriendlyByteBuf buffer)
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