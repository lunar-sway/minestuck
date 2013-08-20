package com.mraof.minestuck.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.IConnectionListener;
import com.mraof.minestuck.util.SburbConnection;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class SburbServerOpenPacket extends MinestuckPacket implements IConnectionListener {

	public String connectedTo;
	private Player player;
	private TileEntityComputer te;
	
	public SburbServerOpenPacket() 
	{
		super(Type.SBURB_OPEN);
	}

	@Override
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeChars((String) data[0]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);

		connectedTo = dat.readLine();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName)
	{
		this.player = player;
		
		SburbConnection conn = new SburbConnection(connectedTo,false);
		conn.addListener(this);
		SburbConnection.addServer(conn);
	}

	@Override
	public void onConnected(SburbConnection conn) {
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.SBURB_CONNECT, conn.getClientPlayer(),conn.getServerPlayer());
		packet.length = packet.data.length;
		PacketDispatcher.sendPacketToPlayer(packet, player);
	}

}
