package com.mraof.minestuck.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.SburbConnection;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class SburbConnectPacket extends MinestuckPacket {

	public String client;
	public String server;
	public SburbConnectPacket() 
	{
		super(Type.SBURB_CONNECT);
	}

	@Override
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeChars((String) data[0]);
		dat.writeChars((String) data[1]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);

		client = dat.readLine();
		server = dat.readLine();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName)
	{
		SburbConnection.connect(client,server);
		Debug.print("Got connect packet");
		
		if (!Minecraft.getMinecraft().theWorld.isRemote) {
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_OPEN,client,server);
			packet.length = packet.data.length;
			PacketDispatcher.sendPacketToAllPlayers(packet);
		}
	}

}
