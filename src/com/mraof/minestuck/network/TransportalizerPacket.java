package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TransportalizerPacket implements PlayToServerPacket
{
	BlockPos pos;
	String destId;
	
	public TransportalizerPacket(BlockPos pos, String destId)
	{
		this.pos = pos;
		this.destId = destId;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeString(destId, 4);
	}
	
	public static TransportalizerPacket decode(PacketBuffer buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		String destId = buffer.readString(4);
		
		return new TransportalizerPacket(pos, destId);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(player.getEntityWorld().isAreaLoaded(pos, 0))
		{
			TileEntity te = player.world.getTileEntity(pos);
			if(te instanceof TransportalizerTileEntity)
			{
				((TransportalizerTileEntity) te).setDestId(destId);
			}
		}
	}
}