package com.mraof.minestuck.network.data;

import com.mraof.minestuck.network.PlayToClientPacket;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;

public class DataCheckerPermissionPacket implements PlayToClientPacket
{
	private final boolean available;
	
	/**
	 * @param available if the player has access to and thus should see the data checker
	 */
	public DataCheckerPermissionPacket(boolean available)
	{
		this.available = available;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(available);
	}
	
	public static DataCheckerPermissionPacket decode(FriendlyByteBuf buffer)
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