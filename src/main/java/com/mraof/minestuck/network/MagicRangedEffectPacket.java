package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.util.MagicEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MagicRangedEffectPacket(MagicEffect.RangedType rangedType, Vec3 pos, Vec3 lookVec, int length, boolean collides) implements MSPacket.PlayToClient
{
		public static final Type<MagicRangedEffectPacket> ID = new Type<>(Minestuck.id("magic_ranged_effect"));
	public static final StreamCodec<FriendlyByteBuf, MagicRangedEffectPacket> STREAM_CODEC = StreamCodec.composite(
			NeoForgeStreamCodecs.enumCodec(MagicEffect.RangedType.class),
			MagicRangedEffectPacket::rangedType,
			MSPayloads.VEC3_STREAM_CODEC,
			MagicRangedEffectPacket::pos,
			MSPayloads.VEC3_STREAM_CODEC,
			MagicRangedEffectPacket::lookVec,
			ByteBufCodecs.INT,
			MagicRangedEffectPacket::length,
			ByteBufCodecs.BOOL,
			MagicRangedEffectPacket::collides,
			MagicRangedEffectPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		MagicEffect.rangedParticleEffect(rangedType, Minecraft.getInstance().level, pos, lookVec, length, collides);
	}
}