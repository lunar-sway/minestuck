package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.StructureCoreBlock;
import com.mraof.minestuck.blockentity.redstone.StructureCoreBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StructureCoreSettingsPacket(StructureCoreBlockEntity.ActionType actionType, int shutdownRange, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	
		public static final Type<StructureCoreSettingsPacket> ID = new Type<>(Minestuck.id("structure_core_settings"));
	public static final StreamCodec<FriendlyByteBuf, StructureCoreSettingsPacket> STREAM_CODEC = StreamCodec.composite(
			NeoForgeStreamCodecs.enumCodec(StructureCoreBlockEntity.ActionType.class),
			StructureCoreSettingsPacket::actionType,
			ByteBufCodecs.INT,
			StructureCoreSettingsPacket::shutdownRange,
			BlockPos.STREAM_CODEC,
			StructureCoreSettingsPacket::beBlockPos,
			StructureCoreSettingsPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		if(!StructureCoreBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, StructureCoreBlockEntity.class)
				.ifPresent(structureCore -> structureCore.handleSettingsPacket(this));
	}
}
