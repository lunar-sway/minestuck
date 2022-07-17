package com.mraof.minestuck.network;

import com.mraof.minestuck.inventory.MachineContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class GoButtonPacket implements PlayToServerPacket
{
	
	private final boolean newMode;
	private final boolean overrideStop;
	
	public GoButtonPacket(boolean newMode, boolean overrideStop)
	{
		this.newMode = newMode;
		this.overrideStop = overrideStop;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(newMode);
		buffer.writeBoolean(overrideStop);
	}
	
	public static GoButtonPacket decode(FriendlyByteBuf buffer)
	{
		boolean newMode = buffer.readBoolean();
		boolean overrideStop = buffer.readBoolean();
		
		return new GoButtonPacket(newMode, overrideStop);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.containerMenu instanceof MachineContainer container)
		{
			System.out.println("Button pressed. Machine going!");
			container.setReady(newMode);
			container.setOverrideStop(overrideStop);
		}
	}
}