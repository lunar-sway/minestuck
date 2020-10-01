package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.management.OpEntry;

public class SburbConnectClosedPacket implements PlayToServerPacket
{
	
	public int player;
	public int otherPlayer;
	public boolean isClient;
	
	public SburbConnectClosedPacket(int player, int otherPlayer, boolean isClient)
	{
		this.player = player;
		this.otherPlayer = otherPlayer;
		this.isClient = isClient;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(player);
		buffer.writeInt(otherPlayer);
		buffer.writeBoolean(isClient);
	}
	
	public static SburbConnectClosedPacket decode(PacketBuffer buffer)
	{
		int player = buffer.readInt();
		int otherPlayer = buffer.readInt();
		boolean isClient = buffer.readBoolean();
		
		return new SburbConnectClosedPacket(player, otherPlayer, isClient);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		OpEntry opsEntry = player.getServer().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
		if((!MinestuckConfig.SERVER.privateComputers.get() || IdentifierHandler.encode(player).getId() == this.player || opsEntry != null && opsEntry.getPermissionLevel() >= 2) && ServerEditHandler.getData(player) == null)
			SkaianetHandler.get(player.world).closeConnection(IdentifierHandler.getById(this.player), otherPlayer != -1 ? IdentifierHandler.getById(this.otherPlayer) : null, isClient);
	}
}