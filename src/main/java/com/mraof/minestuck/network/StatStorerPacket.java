package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.StatStorerTileEntity;
import com.mraof.minestuck.tileentity.WirelessRedstoneTransmitterTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class StatStorerPacket implements PlayToServerPacket
{
	private final StatStorerTileEntity.ActiveType activeType;
	private final BlockPos tileBlockPos;
	//private final String destId;
	
	public StatStorerPacket(StatStorerTileEntity.ActiveType activeType, BlockPos tileBlockPos)
	{
		this.activeType = activeType;
		this.tileBlockPos = tileBlockPos;
		//this.destId = destId;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeEnumValue(activeType);
		buffer.writeBlockPos(tileBlockPos);
		//buffer.writeString(destId, 4);
	}
	
	public static StatStorerPacket decode(PacketBuffer buffer)
	{
		StatStorerTileEntity.ActiveType activeType = buffer.readEnumValue(StatStorerTileEntity.ActiveType.class);
		BlockPos tileBlockPos = buffer.readBlockPos();
		//String destId = buffer.readString(4);
		
		return new StatStorerPacket(activeType, tileBlockPos);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		TileEntity te = player.world.getTileEntity(tileBlockPos);
		if(te instanceof StatStorerTileEntity)
		{
			((StatStorerTileEntity) te).setActiveType(activeType);
		}
	}
}