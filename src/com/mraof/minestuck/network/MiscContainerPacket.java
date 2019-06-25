package com.mraof.minestuck.network;

import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.ContainerHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MiscContainerPacket
{
	
	int i;
	
	public MiscContainerPacket(int i)
	{
		this.i = i;
	}
	
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(i);
	}
	
	public static MiscContainerPacket decode(PacketBuffer buffer)
	{
		int i = buffer.readInt();
		
		return new MiscContainerPacket(i);
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute(EntityPlayerMP player)
	{
		player.openContainer = ContainerHandler.getPlayerStatsContainer(player, i, ServerEditHandler.getData(player) != null);
		player.openContainer.windowId = ContainerHandler.windowIdStart + i;
		player.addSelfToInternalCraftingInventory();    //Must be placed after setting the window id!!
	}
}