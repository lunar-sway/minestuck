package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.management.OpEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SburbConnectPacket
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
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute(ServerPlayerEntity player)
	{
		if(player.world.isAreaLoaded(computer, 0))
		{
			TileEntity te = player.world.getTileEntity(computer);
			if(te instanceof ComputerTileEntity)
			{
				ComputerTileEntity computer = (ComputerTileEntity) te;
				OpEntry opsEntry = player.getServer().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
				if((!MinestuckConfig.privateComputers.get() || IdentifierHandler.encode(player) == computer.owner || opsEntry != null && opsEntry.getPermissionLevel() >= 2) && ServerEditHandler.getData(player) == null)
					SkaianetHandler.get(player.world).requestConnection(computer.owner, GlobalPos.of(computer.getWorld().dimension.getType(), computer.getPos()), otherPlayer != -1 ? IdentifierHandler.getById(otherPlayer) : null, isClient);
			}
		}
	}
}