package com.mraof.minestuck.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class MinestuckPacketHandler implements IPacketHandler 
{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) 
	{
		MinestuckPacket minestuckPacket = MinestuckPacket.readPacket(manager, packet.data, ((EntityPlayer)player).worldObj.isRemote?Side.CLIENT:Side.SERVER);
		if(minestuckPacket == null)
			return;
		String username = "";
		minestuckPacket.execute(manager, this, player, username);
	}

}
