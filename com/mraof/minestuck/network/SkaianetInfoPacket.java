package com.mraof.minestuck.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.skaianet.SkaiaClient;

import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.Player;

public class SkaianetInfoPacket extends MinestuckPacket {

	public SkaianetInfoPacket() {
		super(Type.SBURB_CONNECT);
	}

	@Override
	public byte[] generatePacket(Object... data) {
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.write(((String)data[0]).getBytes());	//Player name
		if(data.length == 1)	//If request from client
			return dat.toByteArray();
		
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName) {
		
		SkaiaClient.consumePacket(this);
		
	}

}
