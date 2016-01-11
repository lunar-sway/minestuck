package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.UsernameHandler;

public class SburbConnectClosedPacket extends MinestuckPacket {
	
	public String player;
	public String otherPlayer;
	public boolean isClient;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat) {
		writeString(data,dat[0].toString());
		data.writeChar('\n');
		writeString(data,dat[1].toString());
		data.writeChar('\n');
		data.writeBoolean((Boolean)dat[2]);
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) {

		player = readLine(data);
		otherPlayer = readLine(data);
		isClient = data.readBoolean();
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if((!MinestuckConfig.privateComputers || UsernameHandler.encode(player.getName()).equals(this.player)) && ServerEditHandler.getData(((EntityPlayer)player).getName()) == null)
			SkaianetHandler.closeConnection(this.player,this.otherPlayer, isClient);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}

}
