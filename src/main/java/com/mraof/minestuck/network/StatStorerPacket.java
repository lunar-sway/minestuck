package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.redstone.StatStorerTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class StatStorerPacket implements PlayToServerPacket
{
	private final StatStorerTileEntity.ActiveType activeType;
	private final BlockPos tileBlockPos;
	private int divideValueBy;
	
	public StatStorerPacket(StatStorerTileEntity.ActiveType activeType, BlockPos tileBlockPos, int divideValueBy)
	{
		this.activeType = activeType;
		this.tileBlockPos = tileBlockPos;
		this.divideValueBy = divideValueBy;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeEnumValue(activeType);
		buffer.writeBlockPos(tileBlockPos);
		buffer.writeInt(divideValueBy);
	}
	
	public static StatStorerPacket decode(PacketBuffer buffer)
	{
		StatStorerTileEntity.ActiveType activeType = buffer.readEnumValue(StatStorerTileEntity.ActiveType.class);
		BlockPos tileBlockPos = buffer.readBlockPos();
		int divideValueBy = buffer.readInt();
		
		return new StatStorerPacket(activeType, tileBlockPos, divideValueBy);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		TileEntity te = player.world.getTileEntity(tileBlockPos);
		if(te instanceof StatStorerTileEntity)
		{
			((StatStorerTileEntity) te).setActiveType(activeType);
			if(divideValueBy <= 0)
				divideValueBy = 1;
			((StatStorerTileEntity) te).setDivideValue(divideValueBy);
		}
	}
}