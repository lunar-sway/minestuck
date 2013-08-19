package com.mraof.minestuck.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.tileentity.TileEntityComputer;

import cpw.mods.fml.common.network.Player;

public class SburbGiveItemsPacket extends MinestuckPacket {

	public String connectedTo;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int gristTotal;
	public SburbGiveItemsPacket() 
	{
		super(Type.SBURB_CONNECT);
	}

	@Override
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeInt((Integer)data[0]);
		dat.writeInt((Integer)data[1]);
		dat.writeInt((Integer)data[2]);
		dat.writeChars((String) data[3]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		
		xCoord = dat.readInt();
		yCoord = dat.readInt();
		zCoord = dat.readInt();
		connectedTo = dat.readLine();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName)
	{
		TileEntityComputer te = (TileEntityComputer)Minecraft.getMinecraft().theWorld.getBlockTileEntity(xCoord,yCoord,zCoord);
				
		if (te == null) {
			System.out.println("[MINESTUCK] Packet recieved, but TE isn't there!");
			return;
		}
		
		te.connectedTo = connectedTo;
		te.connected = true;
		
		System.out.println("[MINESTUCK] Packet recieved. Connection set!");
	}

}
