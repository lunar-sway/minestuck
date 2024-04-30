package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record EditmodeCacheLimitPacket(long limit) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("editmode_cache_limit");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeLong(this.limit);
	}
	
	public static EditmodeCacheLimitPacket read(FriendlyByteBuf buffer)
	{
		return new EditmodeCacheLimitPacket(buffer.readLong());
	}
	
	@Override
	public void execute()
	{
		ClientPlayerData.handleDataPacket(this);
	}
}
