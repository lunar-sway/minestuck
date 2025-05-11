package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.StatStorerBlock;
import com.mraof.minestuck.blockentity.redstone.StatStorerBlockEntity;
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

public record StatStorerSettingsPacket(StatStorerBlockEntity.ActiveType activeType, int divideValueBy, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	
		public static final Type<StatStorerSettingsPacket> ID = new Type<>(Minestuck.id("stat_storer_settings"));
	public static final StreamCodec<FriendlyByteBuf, StatStorerSettingsPacket> STREAM_CODEC = StreamCodec.composite(
			NeoForgeStreamCodecs.enumCodec(StatStorerBlockEntity.ActiveType.class),
			StatStorerSettingsPacket::activeType,
			ByteBufCodecs.INT,
			StatStorerSettingsPacket::divideValueBy,
			BlockPos.STREAM_CODEC,
			StatStorerSettingsPacket::beBlockPos,
			StatStorerSettingsPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		if(!StatStorerBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, StatStorerBlockEntity.class)
				.ifPresent(statStorer -> statStorer.handleSettingsPacket(this));
	}
}
