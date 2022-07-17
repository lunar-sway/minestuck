package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class TransportalizerPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	private final String destId;
	
	public TransportalizerPacket(BlockPos pos, String destId)
	{
		this.pos = pos;
		this.destId = destId;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeUtf(destId, 4);
	}
	
	public static TransportalizerPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		String destId = buffer.readUtf(4);
		
		return new TransportalizerPacket(pos, destId);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.getCommandSenderWorld().isAreaLoaded(pos, 0))
		{
			if(player.level.getBlockEntity(pos) instanceof TransportalizerTileEntity transportalizer)
			{
				transportalizer.setDestId(destId);
			}
		}
	}
}