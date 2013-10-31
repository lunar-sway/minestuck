package com.mraof.minestuck.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.EditHandler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.Player;

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
	public MinestuckPacket consumePacket(byte[] data) {
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
			EditHandler.onPlayerExit(playerMP);
		if(!Minestuck.privateComputers || playerMP.username.equals(this.username))
			EditHandler.newServerEditor(playerMP, username, target);
	}

}
