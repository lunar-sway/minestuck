package com.mraof.minestuck.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class MinestuckPacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) 
	{
		MinestuckPacket minestuckPacket = MinestuckPacket.readPacket(manager, packet.data);
		if(minestuckPacket == null)
			return;
		String username = "";
		minestuckPacket.execute(manager, this, player, username);
	}

}
