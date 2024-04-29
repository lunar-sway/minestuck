package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.redstone.WirelessRedstoneTransmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record WirelessRedstoneTransmitterPacket(BlockPos destinationBlockPos, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("wireless_redstone_transmitter");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(destinationBlockPos);
		buffer.writeBlockPos(beBlockPos);
	}
	
	public static WirelessRedstoneTransmitterPacket read(FriendlyByteBuf buffer)
	{
		BlockPos destinationBlockPos = buffer.readBlockPos();
		BlockPos beBlockPos = buffer.readBlockPos();
		
		return new WirelessRedstoneTransmitterPacket(destinationBlockPos, beBlockPos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.level().isAreaLoaded(beBlockPos, 0)
				&& player.level().getBlockEntity(beBlockPos) instanceof WirelessRedstoneTransmitterBlockEntity transmitter
				&& Math.sqrt(player.distanceToSqr(beBlockPos.getX() + 0.5, beBlockPos.getY() + 0.5, beBlockPos.getZ() + 0.5)) <= 8)
		{
			transmitter.setOffsetFromDestinationBlockPos(destinationBlockPos);
		}
	}
}