package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.PunchDesignixBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record TriggerPunchDesignixPacket(String captcha, BlockPos pos) implements MSPacket.PlayToServer
{
	
		public static final Type<TriggerPunchDesignixPacket> ID = new Type<>(Minestuck.id("trigger_punch_designix"));
	public static final StreamCodec<FriendlyByteBuf, TriggerPunchDesignixPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			TriggerPunchDesignixPacket::captcha,
			BlockPos.STREAM_CODEC,
			TriggerPunchDesignixPacket::pos,
			TriggerPunchDesignixPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		MSPacket.getAccessibleBlockEntity(player, this.pos, PunchDesignixBlockEntity.class).ifPresent(punchDesignix ->
		{
			punchDesignix.setCaptcha(captcha);
			punchDesignix.punchCard(player);
		});
	}
}
