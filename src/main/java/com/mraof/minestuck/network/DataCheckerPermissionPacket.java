package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

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
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBoolean(available);
	}
	
	public static DataCheckerPermissionPacket decode(PacketBuffer buffer)
	{
		boolean dataChecker = buffer.readBoolean();
		
		return new DataCheckerPermissionPacket(dataChecker);
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(this::execute);
		
		ctx.get().setPacketHandled(true);
	}
	
	@Override
	public void execute()
	{
		MinestuckConfig.dataCheckerAccess = available;
	}
}