package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.OpEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class SburbConnectPacket implements PlayToServerPacket
{
	
	private final BlockPos computer;
	private final int otherPlayer;
	private final boolean isClient;
	
	public SburbConnectPacket(BlockPos computer, int otherPlayer, boolean isClient)
	{
		this.computer = computer;
		this.otherPlayer = otherPlayer;
		this.isClient = isClient;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(computer);
		buffer.writeInt(otherPlayer);
		buffer.writeBoolean(isClient);
	}
	
	public static SburbConnectPacket decode(PacketBuffer buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		int otherPlayer = buffer.readInt();
		boolean isClient = buffer.readBoolean();
		
		return new SburbConnectPacket(computer, otherPlayer, isClient);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(player.world.isAreaLoaded(computer, 0))
		{
			TileEntity te = player.world.getTileEntity(computer);
			if(te instanceof ComputerTileEntity)
			{
				ComputerTileEntity computer = (ComputerTileEntity) te;
				MinecraftServer mcServer = Objects.requireNonNull(player.getServer());
				OpEntry opsEntry = mcServer.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
				if((!MinestuckConfig.privateComputers.get() || IdentifierHandler.encode(player) == computer.owner || opsEntry != null && opsEntry.getPermissionLevel() >= 2) && ServerEditHandler.getData(player) == null)
					SkaianetHandler.get(mcServer).requestConnection(computer.owner, ComputerReference.of(computer), otherPlayer != -1 ? IdentifierHandler.getById(otherPlayer) : null, isClient);
			}
		}
	}
}