package com.mraof.minestuck.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class SburbConnectClosedPacket extends MinestuckPacket {
	
	public String player;
	public String otherPlayer;
	public boolean isClient;
	
	public SburbConnectClosedPacket() {
		super(Type.SBURB_CLOSE);
	}

	@Override
	public byte[] generatePacket(Object... data) {
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.write(data[0].toString().getBytes());
		dat.write('\n');
		dat.write(data[1].toString().getBytes());
		dat.write('\n');
		dat.writeBoolean((Boolean)data[2]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);

		player = dat.readLine();
		otherPlayer = dat.readLine();
		isClient = dat.readBoolean();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName) {
		SkaianetHandler.closeConnection(this.player,this.otherPlayer, isClient);
		Debug.print("Got disconnect packet, player:"+this.player+", otherplayer:"+otherPlayer);
		
	}

}
