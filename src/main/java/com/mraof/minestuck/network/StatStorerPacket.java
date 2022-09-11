package com.mraof.minestuck.network;

import com.mraof.minestuck.blockentity.redstone.StatStorerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class StatStorerPacket implements PlayToServerPacket
{
	private final StatStorerTileEntity.ActiveType activeType;
	private final BlockPos tileBlockPos;
	private final int divideValueBy;
	
	public StatStorerPacket(StatStorerTileEntity.ActiveType activeType, BlockPos tileBlockPos, int divideValueBy)
	{
		this.activeType = activeType;
		this.tileBlockPos = tileBlockPos;
		this.divideValueBy = divideValueBy;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeEnum(activeType);
		buffer.writeBlockPos(tileBlockPos);
		buffer.writeInt(divideValueBy);
	}
	
	public static StatStorerPacket decode(FriendlyByteBuf buffer)
	{
		StatStorerTileEntity.ActiveType activeType = buffer.readEnum(StatStorerTileEntity.ActiveType.class);
		BlockPos tileBlockPos = buffer.readBlockPos();
		int divideValueBy = buffer.readInt();
		
		return new StatStorerPacket(activeType, tileBlockPos, divideValueBy);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.level.isAreaLoaded(tileBlockPos, 0)
				&& Math.sqrt(player.distanceToSqr(tileBlockPos.getX() + 0.5, tileBlockPos.getY() + 0.5, tileBlockPos.getZ() + 0.5)) <= 8)
		{
			BlockEntity te = player.level.getBlockEntity(tileBlockPos);
			
			if(te instanceof StatStorerTileEntity statStorer)
			{
				int largestDivideValueBy = Math.max(1, divideValueBy); //should not be able to enter 0 or negative number range
				statStorer.setActiveTypeAndDivideValue(activeType, largestDivideValueBy);
			}
		}
	}
}