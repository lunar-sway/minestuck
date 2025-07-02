package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleAspectEffectsPacket() implements MSPacket.PlayToServer
{
	
		public static final Type<ToggleAspectEffectsPacket> ID = new Type<>(Minestuck.id("toggle_aspect_effects"));
	
	public static final String ON = "minestuck.aspect_effects.on";
	public static final String OFF = "minestuck.aspect_effects.off";
	public static final StreamCodec<FriendlyByteBuf, ToggleAspectEffectsPacket> STREAM_CODEC = StreamCodec.unit(new ToggleAspectEffectsPacket());
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		boolean newToggle = !player.getData(MSAttachments.EFFECT_TOGGLE);
		player.setData(MSAttachments.EFFECT_TOGGLE, newToggle);
		
		MutableComponent message = newToggle
				? Component.translatable(ON)
				: Component.translatable(OFF);
		player.displayClientMessage(message, true);
	}
}
