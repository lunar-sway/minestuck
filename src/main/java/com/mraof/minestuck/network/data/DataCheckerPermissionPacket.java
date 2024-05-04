package com.mraof.minestuck.network.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class DataCheckerPermissionPacket implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("data_checker_permission");
	
	private final boolean available;
	
	/**
	 * @param available if the player has access to and thus should see the data checker
	 */
	public DataCheckerPermissionPacket(boolean available)
	{
		this.available = available;
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(available);
	}
	
	public static DataCheckerPermissionPacket read(FriendlyByteBuf buffer)
	{
		boolean dataChecker = buffer.readBoolean();
		
		return new DataCheckerPermissionPacket(dataChecker);
	}
	
	@Override
	public void execute()
	{
		ClientPlayerData.handleDataPacket(this);
	}
	
	public boolean isDataCheckerAvailable()
	{
		return available;
	}
}