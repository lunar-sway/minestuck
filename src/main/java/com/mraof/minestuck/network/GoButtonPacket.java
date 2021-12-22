package com.mraof.minestuck.network;

import com.mraof.minestuck.inventory.MachineContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

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
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBoolean(newMode);
		buffer.writeBoolean(overrideStop);
	}
	
	public static GoButtonPacket decode(PacketBuffer buffer)
	{
		boolean newMode = buffer.readBoolean();
		boolean overrideStop = buffer.readBoolean();
		
		return new GoButtonPacket(newMode, overrideStop);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(player.containerMenu instanceof MachineContainer)
		{
			MachineContainer container = (MachineContainer) player.containerMenu;
			System.out.println("Button pressed. Machine going!");
			container.setReady(newMode);
			container.setOverrideStop(overrideStop);
		}
	}
}