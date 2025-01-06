package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.BlockTeleporterBlock;
import com.mraof.minestuck.blockentity.redstone.BlockTeleporterBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BlockTeleporterSettingsPacket(BlockPos offsetPos, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	
		public static final Type<BlockTeleporterSettingsPacket> ID = new Type<>(Minestuck.id("block_teleporter_settings"));
	public static final StreamCodec<FriendlyByteBuf, BlockTeleporterSettingsPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			BlockTeleporterSettingsPacket::offsetPos,
			BlockPos.STREAM_CODEC,
			BlockTeleporterSettingsPacket::beBlockPos,
			BlockTeleporterSettingsPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		if(!BlockTeleporterBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, BlockTeleporterBlockEntity.class)
				.ifPresent(blockTeleporter -> blockTeleporter.handleSettingsPacket(this));
	}
}
