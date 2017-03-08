package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.skaianet.ComputerData;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;

public class SburbConnectPacket extends MinestuckPacket
{
	
	ComputerData player;
	int otherPlayer;
	boolean isClient;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		ComputerData compData = (ComputerData)dat[0];
		data.writeInt(compData.getOwnerId());
		data.writeInt(compData.getX());
		data.writeInt(compData.getY());
		data.writeInt(compData.getZ());
		data.writeInt(compData.getDimension());
		data.writeInt((Integer)dat[1]);
		data.writeBoolean((Boolean)dat[2]);
		
		return this;
	}
	
	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		player = new ComputerData(IdentifierHandler.getById(data.readInt()), data.readInt(), data.readInt(), data.readInt(), data.readInt());
		otherPlayer = data.readInt();
		isClient = data.readBoolean();
		
		return this;
	}
	
	@Override
	public void execute(EntityPlayer player)
	{
		UserListOpsEntry opsEntry = player.getServer().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
		if((!MinestuckConfig.privateComputers || IdentifierHandler.encode(player) == this.player.getOwner() || opsEntry != null && opsEntry.getPermissionLevel() >= 2) && ServerEditHandler.getData(player) == null)
			SkaianetHandler.requestConnection(this.player, otherPlayer != -1 ? IdentifierHandler.getById(otherPlayer) : null, isClient);
	}
	
	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
}