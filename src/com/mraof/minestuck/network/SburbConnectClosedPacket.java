package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SburbConnectClosedPacket
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
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute(EntityPlayerMP player)
	{
		UserListOpsEntry opsEntry = player.getServer().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
		if((!MinestuckConfig.privateComputers || IdentifierHandler.encode(player).getId() == this.player || opsEntry != null && opsEntry.getPermissionLevel() >= 2) && ServerEditHandler.getData(player) == null)
			SkaianetHandler.closeConnection(player.getServer(), IdentifierHandler.getById(this.player), otherPlayer != -1 ? IdentifierHandler.getById(this.otherPlayer) : null, isClient);
	}
}