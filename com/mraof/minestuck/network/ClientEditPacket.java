package com.mraof.minestuck.network;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.editmode.ServerEditHandler;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class ClientEditPacket extends MinestuckPacket {
	
	String username;
	String target;
	
	public ClientEditPacket() {
		super(Type.CLIENT_EDIT);
	}

	@Override
	public byte[] generatePacket(Object... data) {
		return data.length == 0?new byte[0]:(data[0].toString()+"\n"+data[1].toString()).getBytes();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data, Side side) {
		if(data.length == 0)
			return this;
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		username = dat.readLine();
		target = dat.readLine();
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName) {
		EntityPlayerMP playerMP = (EntityPlayerMP)player;
		if(username == null)
			ServerEditHandler.onPlayerExit(playerMP);
		if(!Minestuck.privateComputers || playerMP.username.equals(this.username))
			ServerEditHandler.newServerEditor(playerMP, username, target);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}

}
