package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StopCreativeShockEffectPacket(boolean mayBuild) implements MSPacket.PlayToClient
{
	
		public static final Type<StopCreativeShockEffectPacket> ID = new Type<>(Minestuck.id("stop_creative_shock_effect"));
	public static final StreamCodec<ByteBuf, StopCreativeShockEffectPacket> STREAM_CODEC = ByteBufCodecs.BOOL.map(StopCreativeShockEffectPacket::new, StopCreativeShockEffectPacket::mayBuild);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		Player playerEntity = ClientProxy.getClientPlayer();
		if(playerEntity != null)
		{
			playerEntity.getAbilities().mayBuild = !mayBuild;
		}
	}
}
