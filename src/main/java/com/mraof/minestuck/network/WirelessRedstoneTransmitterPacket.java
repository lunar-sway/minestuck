package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.redstone.WirelessRedstoneTransmitterTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

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
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(destinationBlockPos);
		buffer.writeBlockPos(tileBlockPos);
	}
	
	public static WirelessRedstoneTransmitterPacket decode(PacketBuffer buffer)
	{
		BlockPos destinationBlockPos = buffer.readBlockPos();
		BlockPos tileBlockPos = buffer.readBlockPos();
		
		return new WirelessRedstoneTransmitterPacket(destinationBlockPos, tileBlockPos);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(player.level.isAreaLoaded(tileBlockPos, 0))
		{
			TileEntity te = player.level.getBlockEntity(tileBlockPos);
			if(te instanceof WirelessRedstoneTransmitterTileEntity)
			{
				BlockPos tePos = te.getBlockPos();
				if(Math.sqrt(player.distanceToSqr(tePos.getX() + 0.5, tePos.getY() + 0.5, tePos.getZ() + 0.5)) <= 8)
				{
					((WirelessRedstoneTransmitterTileEntity) te).setDestinationBlockPos(destinationBlockPos);
				}
			}
		}
	}
}