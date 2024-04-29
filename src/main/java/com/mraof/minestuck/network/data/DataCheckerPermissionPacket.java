package com.mraof.minestuck.network.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record DataCheckerPermissionPacket(boolean isAvailable) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("data_checker_permission");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(isAvailable);
	}
	
	public static DataCheckerPermissionPacket read(FriendlyByteBuf buffer)
	{
		boolean isAvailable = buffer.readBoolean();
		
		return new DataCheckerPermissionPacket(isAvailable);
	}
	
	@Override
	public void execute()
	{
		ClientPlayerData.handleDataPacket(this);
	}
}