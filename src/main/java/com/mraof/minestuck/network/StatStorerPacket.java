package com.mraof.minestuck.network;

import com.mraof.minestuck.blockentity.redstone.StatStorerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class StatStorerPacket implements PlayToServerPacket
{
	private final StatStorerBlockEntity.ActiveType activeType;
	private final BlockPos beBlockPos;
	private final int divideValueBy;
	
	public StatStorerPacket(StatStorerBlockEntity.ActiveType activeType, BlockPos beBlockPos, int divideValueBy)
	{
		this.activeType = activeType;
		this.beBlockPos = beBlockPos;
		this.divideValueBy = divideValueBy;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeEnum(activeType);
		buffer.writeBlockPos(beBlockPos);
		buffer.writeInt(divideValueBy);
	}
	
	public static StatStorerPacket decode(FriendlyByteBuf buffer)
	{
		StatStorerBlockEntity.ActiveType activeType = buffer.readEnum(StatStorerBlockEntity.ActiveType.class);
		BlockPos beBlockPos = buffer.readBlockPos();
		int divideValueBy = buffer.readInt();
		
		return new StatStorerPacket(activeType, beBlockPos, divideValueBy);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.level().isAreaLoaded(beBlockPos, 0)
				&& Math.sqrt(player.distanceToSqr(beBlockPos.getX() + 0.5, beBlockPos.getY() + 0.5, beBlockPos.getZ() + 0.5)) <= 8)
		{
			BlockEntity te = player.level().getBlockEntity(beBlockPos);
			
			if(te instanceof StatStorerBlockEntity statStorer)
			{
				int largestDivideValueBy = Math.max(1, divideValueBy); //should not be able to enter 0 or negative number range
				statStorer.setActiveTypeAndDivideValue(activeType, largestDivideValueBy);
			}
		}
	}
}