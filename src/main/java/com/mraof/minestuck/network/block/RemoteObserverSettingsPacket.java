package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.RemoteObserverBlock;
import com.mraof.minestuck.blockentity.redstone.RemoteObserverBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;
import java.util.Optional;

public record RemoteObserverSettingsPacket(RemoteObserverBlockEntity.ActiveType activeType, int observingRange, BlockPos beBlockPos, Optional<EntityType<?>> entityType) implements MSPacket.PlayToServer
{
	
		public static final Type<RemoteObserverSettingsPacket> ID = new Type<>(Minestuck.id("remote_observer_settings"));
	public static final StreamCodec<RegistryFriendlyByteBuf, RemoteObserverSettingsPacket> STREAM_CODEC = StreamCodec.composite(
			NeoForgeStreamCodecs.enumCodec(RemoteObserverBlockEntity.ActiveType.class),
			RemoteObserverSettingsPacket::activeType,
			ByteBufCodecs.INT,
			RemoteObserverSettingsPacket::observingRange,
			BlockPos.STREAM_CODEC,
			RemoteObserverSettingsPacket::beBlockPos,
			ByteBufCodecs.optional(ByteBufCodecs.registry(Registries.ENTITY_TYPE)),
			RemoteObserverSettingsPacket::entityType,
			RemoteObserverSettingsPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		if(!RemoteObserverBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, RemoteObserverBlockEntity.class)
				.ifPresent(observerBE -> observerBE.handleSettingsPacket(this));
	}
}
