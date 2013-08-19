package com.mraof.minestuck.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.SburbConnector;

import cpw.mods.fml.common.network.Player;

public class SburbServerOpenPacket extends MinestuckPacket {

	public String connectedTo;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int gristTotal;
	public SburbServerOpenPacket() 
	{
		super(Type.SBURB_CONNECT);
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
		SburbConnector.addServer(connectedTo);
	}

}
