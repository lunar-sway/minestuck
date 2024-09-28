package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.WirelessRedstoneTransmitterBlock;
import com.mraof.minestuck.blockentity.redstone.WirelessRedstoneTransmitterBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record WirelessRedstoneTransmitterSettingsPacket(BlockPos destinationBlockPos, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("wireless_redstone_transmitter_settings");
	
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
	
	public static WirelessRedstoneTransmitterSettingsPacket read(FriendlyByteBuf buffer)
	{
		BlockPos destinationBlockPos = buffer.readBlockPos();
		BlockPos beBlockPos = buffer.readBlockPos();
		
		return new WirelessRedstoneTransmitterSettingsPacket(destinationBlockPos, beBlockPos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(!WirelessRedstoneTransmitterBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, WirelessRedstoneTransmitterBlockEntity.class)
				.ifPresent(transmitter -> transmitter.handleSettingsPacket(this));
	}
}
