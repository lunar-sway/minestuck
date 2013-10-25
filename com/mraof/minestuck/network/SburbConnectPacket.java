package com.mraof.minestuck.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.skaianet.ComputerData;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;

import cpw.mods.fml.common.network.Player;

public class SburbConnectPacket extends MinestuckPacket {

	ComputerData player;
	String otherPlayer;
	boolean isClient;
	
	public SburbConnectPacket() 
	{
		super(Type.SBURB_CONNECT);
	}

	@Override
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		ComputerData compData = (ComputerData)data[0];
		dat.write((compData.getOwner()+'\n').getBytes());
		dat.writeInt(compData.getX());
		dat.writeInt(compData.getY());
		dat.writeInt(compData.getZ());
		dat.writeInt(compData.getDimension());
		dat.write((data[1].toString()+'\n').getBytes());
		dat.writeBoolean((Boolean)data[2]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);

		player = new ComputerData(dat.readLine(), dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt());
		otherPlayer = dat.readLine();
		isClient = dat.readBoolean();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName) {
		if(!Minestuck.privateComputers || ((EntityPlayer)player).username.equals(this.player))
			SkaianetHandler.requestConnection(this.player, otherPlayer, isClient);
	}

}
