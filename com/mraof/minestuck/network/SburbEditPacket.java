package com.mraof.minestuck.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.util.EditHandler;

import net.minecraft.entity.player.EntityPlayer;
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
		return data.length == 0?new byte[0]:(data[0].toString()+'\n'+data[1].toString()).getBytes();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) {
		if(data.length == 0)
			return this;
		ByteArrayDataInput input = ByteStreams.newDataInput(data);
		username = input.readLine();
		target = input.readLine();
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName) {
		if(((EntityPlayer)player).worldObj.isRemote){
			EditHandler.resetClient();
		} else{
			EntityPlayerMP playerMP = (EntityPlayerMP)player;
			if(username == null)
				EditHandler.onPlayerExit(playerMP);
			if(!Minestuck.privateComputers || playerMP.username.equals(this.username))
				EditHandler.newServerEditor(playerMP, username, target);
		}
	}

}
