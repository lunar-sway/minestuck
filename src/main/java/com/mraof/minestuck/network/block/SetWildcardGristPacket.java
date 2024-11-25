package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.blockentity.machine.GristWildcardHolder;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetWildcardGristPacket(BlockPos pos, GristType gristType) implements MSPacket.PlayToServer
{
	
		public static final Type<SetWildcardGristPacket> ID = new Type<>(Minestuck.id("set_wildcard_grist"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SetWildcardGristPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			SetWildcardGristPacket::pos,
			GristType.STREAM_CODEC,
			SetWildcardGristPacket::gristType,
			SetWildcardGristPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		MSPacket.getAccessibleBlockEntity(player, this.pos, GristWildcardHolder.class)
				.ifPresent(blockEntity -> blockEntity.setWildcardGrist(gristType));
	}
}
