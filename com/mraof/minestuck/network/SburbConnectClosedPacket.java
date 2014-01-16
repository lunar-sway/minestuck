package com.mraof.minestuck.network;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

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
		if(!Minestuck.privateComputers || ((EntityPlayer)player).username.equals(this.player) && ServerEditHandler.getData(((EntityPlayer)player).username) == null)
			SkaianetHandler.closeConnection(this.player,this.otherPlayer, isClient);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}

}
