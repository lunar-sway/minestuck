package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.redstone.RemoteObserverTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class RemoteObserverPacket implements PlayToServerPacket
{
	private final RemoteObserverTileEntity.ActiveType activeType;
	private final BlockPos tileBlockPos;
	
	public RemoteObserverPacket(RemoteObserverTileEntity.ActiveType activeType, BlockPos tileBlockPos)
	{
		this.activeType = activeType;
		this.tileBlockPos = tileBlockPos;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeEnumValue(activeType);
		buffer.writeBlockPos(tileBlockPos);
	}
	
	public static RemoteObserverPacket decode(PacketBuffer buffer)
	{
		RemoteObserverTileEntity.ActiveType activeType = buffer.readEnumValue(RemoteObserverTileEntity.ActiveType.class);
		BlockPos tileBlockPos = buffer.readBlockPos();
		
		return new RemoteObserverPacket(activeType, tileBlockPos);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		TileEntity te = player.world.getTileEntity(tileBlockPos);
		if(te instanceof RemoteObserverTileEntity)
		{
			((RemoteObserverTileEntity) te).setActiveType(activeType);
		}
	}
}