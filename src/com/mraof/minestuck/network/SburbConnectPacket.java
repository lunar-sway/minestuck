package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.skaianet.ComputerData;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.UsernameHandler;

import cpw.mods.fml.relauncher.Side;

public class SburbConnectPacket extends MinestuckPacket {

	ComputerData player;
	String otherPlayer;
	boolean isClient;
	
	public SburbConnectPacket() 
	{
		super(Type.SBURB_CONNECT);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		ComputerData compData = (ComputerData)dat[0];
		writeString(data,compData.getOwner()+'\n');
		data.writeInt(compData.getX());
		data.writeInt(compData.getY());
		data.writeInt(compData.getZ());
		data.writeInt(compData.getDimension());
		writeString(data,dat[1].toString()+'\n');
		data.writeBoolean((Boolean)dat[2]);
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		player = new ComputerData(readLine(data), data.readInt(), data.readInt(), data.readInt(), data.readInt());
		otherPlayer = readLine(data);
		isClient = data.readBoolean();
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player) {
		if((!Minestuck.privateComputers || (UsernameHandler.encode(player.getCommandSenderName()).equals(this.player.getOwner()))) && ServerEditHandler.getData(((EntityPlayer)player).getCommandSenderName()) == null)
			SkaianetHandler.requestConnection(this.player, otherPlayer, isClient);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}

}
