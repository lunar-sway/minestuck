package com.mraof.minestuck.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.network.MinestuckPacket.Type;

import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class MinestuckConnectHandler implements IConnectionHandler 
{

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) 
	{
		//set all the grist values to the correct amount
        Packet250CustomPayload packet = new Packet250CustomPayload();
        int[] gristValues = new int[EntityGrist.gristTypes.length];
        for(int typeInt = 0; typeInt < gristValues.length; typeInt++)
        	gristValues[typeInt] = ((EntityPlayer)player).getEntityData().getCompoundTag("Grist").getInteger(EntityGrist.gristTypes[typeInt]);
        packet.channel = "Minestuck";
        packet.data = MinestuckPacket.makePacket(Type.GRISTCACHE, gristValues);
        packet.length = packet.data.length;
        ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) 
	{
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) 
	{
		
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) 
	{

	}

	@Override
	public void connectionClosed(INetworkManager manager) 
	{

	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) 
	{

	}

}
