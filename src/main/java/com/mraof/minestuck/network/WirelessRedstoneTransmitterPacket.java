package com.mraof.minestuck.network;

import com.mraof.minestuck.blockentity.redstone.WirelessRedstoneTransmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class WirelessRedstoneTransmitterPacket implements MSPacket.PlayToServer
{
	private final BlockPos destinationBlockPos;
	private final BlockPos beBlockPos;
	
	public WirelessRedstoneTransmitterPacket(BlockPos pos, BlockPos beBlockPos)
	{
		this.destinationBlockPos = pos;
		this.beBlockPos = beBlockPos;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(destinationBlockPos);
		buffer.writeBlockPos(beBlockPos);
	}
	
	public static WirelessRedstoneTransmitterPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos destinationBlockPos = buffer.readBlockPos();
		BlockPos beBlockPos = buffer.readBlockPos();
		
		return new WirelessRedstoneTransmitterPacket(destinationBlockPos, beBlockPos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.level().isAreaLoaded(beBlockPos, 0))
		{
			if(player.level().getBlockEntity(beBlockPos) instanceof WirelessRedstoneTransmitterBlockEntity transmitter)
			{
				if(Math.sqrt(player.distanceToSqr(beBlockPos.getX() + 0.5, beBlockPos.getY() + 0.5, beBlockPos.getZ() + 0.5)) <= 8)
				{
					transmitter.setOffsetFromDestinationBlockPos(destinationBlockPos);
				}
			}
		}
	}
}