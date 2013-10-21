package com.mraof.minestuck.network;

import com.google.common.io.ByteStreams;
import com.mraof.minestuck.entity.EntityDecoy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.Player;

public class SburbEditPacket extends MinestuckPacket {
	
	String username;
	
	public SburbEditPacket() {
		super(Type.SBURB_EDIT);
	}

	@Override
	public byte[] generatePacket(Object... data) {
		return data[0].toString().getBytes();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) {
		username = ByteStreams.newDataInput(data).readLine();
		return this;
	}

	@Override
	public void execute(INetworkManager network,
			MinestuckPacketHandler minestuckPacketHandler, Player player,
			String userName) {
		EntityPlayer p = (EntityPlayer)player;
		EntityDecoy decoy = new EntityDecoy(p.worldObj, p);
		decoy.worldObj.spawnEntityInWorld(decoy);
	}

}
