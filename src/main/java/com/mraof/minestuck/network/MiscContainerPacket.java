package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.EditmodeContainer;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

public class MiscContainerPacket implements PlayToServerPacket
{
	
	int i;
	
	public MiscContainerPacket(int i)
	{
		this.i = i;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(i);
	}
	
	public static MiscContainerPacket decode(PacketBuffer buffer)
	{
		int i = buffer.readInt();
		
		return new MiscContainerPacket(i);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(ServerEditHandler.getData(player) == null)
		{
			player.openContainer = new CaptchaDeckContainer(PlayerStatsScreen.WINDOW_ID_START + i, player.inventory);//ContainerHandler.windowIdStart + i;
		} else
		{
			player.openContainer = new EditmodeContainer(PlayerStatsScreen.WINDOW_ID_START + i, player.inventory);
		}
		
		player.addSelfToInternalCraftingInventory();
	}
}