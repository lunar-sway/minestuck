package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.redstone.WirelessRedstoneTransmitterTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class WirelessRedstoneTransmitterPacket implements PlayToServerPacket
{
	private final BlockPos destinationBlockPos;
	private final BlockPos tileBlockPos;
	
	public WirelessRedstoneTransmitterPacket(BlockPos pos, BlockPos tileBlockPos)
	{
		this.destinationBlockPos = pos;
		this.tileBlockPos = tileBlockPos;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(destinationBlockPos);
		buffer.writeBlockPos(tileBlockPos);
	}
	
	public static WirelessRedstoneTransmitterPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos destinationBlockPos = buffer.readBlockPos();
		BlockPos tileBlockPos = buffer.readBlockPos();
		
		return new WirelessRedstoneTransmitterPacket(destinationBlockPos, tileBlockPos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.level.isAreaLoaded(tileBlockPos, 0))
		{
			if(player.level.getBlockEntity(tileBlockPos) instanceof WirelessRedstoneTransmitterTileEntity transmitter)
			{
				if(Math.sqrt(player.distanceToSqr(tileBlockPos.getX() + 0.5, tileBlockPos.getY() + 0.5, tileBlockPos.getZ() + 0.5)) <= 8)
				{
					transmitter.setOffsetFromDestinationBlockPos(destinationBlockPos);
				}
			}
		}
	}
}