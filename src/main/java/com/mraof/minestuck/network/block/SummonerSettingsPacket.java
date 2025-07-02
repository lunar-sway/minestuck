package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.blockentity.redstone.SummonerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SummonerSettingsPacket(boolean isUntriggerable, int summonRange, EntityType<?> entityType, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	public static final Type<SummonerSettingsPacket> ID = new Type<>(Minestuck.id("summoner_settings"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SummonerSettingsPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL,
			SummonerSettingsPacket::isUntriggerable,
			ByteBufCodecs.INT,
			SummonerSettingsPacket::summonRange,
			ByteBufCodecs.registry(Registries.ENTITY_TYPE),
			SummonerSettingsPacket::entityType,
			BlockPos.STREAM_CODEC,
			SummonerSettingsPacket::beBlockPos,
			SummonerSettingsPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		if(!SummonerBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, SummonerBlockEntity.class)
				.ifPresent(summoner -> summoner.handleSettingsPacket(this));
	}
}
