package com.mraof.minestuck.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.util.EditHandler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.server.MinecraftServer;

import cpw.mods.fml.common.network.Player;

public class SburbEditPacket extends MinestuckPacket {
	
	String username;
	String target;
	
	public SburbEditPacket() {
		super(Type.SBURB_EDIT);
	}

	@Override
	public byte[] generatePacket(Object... data) {
		return (data[0].toString()+'\n'+data[1].toString()).getBytes();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) {
		ByteArrayDataInput input = ByteStreams.newDataInput(data);
		username = input.readLine();
		target = input.readLine();
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName) {
		EntityPlayerMP playerMP = (EntityPlayerMP)player;
		if(!Minestuck.privateComputers || playerMP.username.equals(this.username))
			EditHandler.newServerEditor(playerMP, username, target);
	}

}
