package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.skaianet.ComputerData;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.Location;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SburbConnectPacket
{
	
	private final ComputerData player;
	private final int otherPlayer;
	private final boolean isClient;
	
	public SburbConnectPacket(ComputerData player, int otherPlayer, boolean isClient)
	{
		this.player = player;
		this.otherPlayer = otherPlayer;
		this.isClient = isClient;
	}
	
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(player.getOwnerId());
		player.getLocation().toBuffer(buffer);
		buffer.writeInt(otherPlayer);
		buffer.writeBoolean(isClient);
	}
	
	public static SburbConnectPacket decode(PacketBuffer buffer)
	{
		IdentifierHandler.PlayerIdentifier identifier = IdentifierHandler.getById(buffer.readInt());
		
		ComputerData player = new ComputerData(identifier, Location.fromBuffer(buffer));
		int otherPlayer = buffer.readInt();
		boolean isClient = buffer.readBoolean();
		
		return new SburbConnectPacket(player, otherPlayer, isClient);
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
		if((!MinestuckConfig.privateComputers || IdentifierHandler.encode(player) == this.player.getOwner() || opsEntry != null && opsEntry.getPermissionLevel() >= 2) && ServerEditHandler.getData(player) == null)
			SkaianetHandler.requestConnection(player.getServer(), this.player, otherPlayer != -1 ? IdentifierHandler.getById(otherPlayer) : null, isClient);
	}
}