package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.redstone.WirelessRedstoneTransmitterBlockEntity;
import com.mraof.minestuck.network.MSPacket;
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
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, WirelessRedstoneTransmitterBlockEntity.class)
				.ifPresent(transmitter -> transmitter.setOffsetFromDestinationBlockPos(destinationBlockPos));
	}
}
