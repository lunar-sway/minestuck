package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.WirelessRedstoneTransmitterBlock;
import com.mraof.minestuck.blockentity.redstone.WirelessRedstoneTransmitterBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record WirelessRedstoneTransmitterSettingsPacket(BlockPos destinationBlockPos, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	
		public static final Type<WirelessRedstoneTransmitterSettingsPacket> ID = new Type<>(Minestuck.id("wireless_redstone_transmitter_settings"));
	public static final StreamCodec<FriendlyByteBuf, WirelessRedstoneTransmitterSettingsPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			WirelessRedstoneTransmitterSettingsPacket::destinationBlockPos,
			BlockPos.STREAM_CODEC,
			WirelessRedstoneTransmitterSettingsPacket::beBlockPos,
			WirelessRedstoneTransmitterSettingsPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		if(!WirelessRedstoneTransmitterBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, WirelessRedstoneTransmitterBlockEntity.class)
				.ifPresent(transmitter -> transmitter.handleSettingsPacket(this));
	}
}
