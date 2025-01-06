package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BoondollarDataPacket(long amount) implements MSPacket.PlayToClient
{
	public static final Type<BoondollarDataPacket> ID = new Type<>(Minestuck.id("boondollar_data"));
	public static final StreamCodec<RegistryFriendlyByteBuf, BoondollarDataPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_LONG,
			BoondollarDataPacket::amount,
			BoondollarDataPacket::new
	);
	
	public static BoondollarDataPacket create(long count)
	{
		return new BoondollarDataPacket(count);
	}
	
	@Override
	public @NotNull Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	public static BoondollarDataPacket read(FriendlyByteBuf buffer)
	{
		long count = buffer.readLong();
		return create(count);
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		ClientPlayerData.handleDataPacket(this);
	}
}
