package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.util.MagicEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MagicAOEEffectPacket(MagicEffect.AOEType aoeType, Vec3 minAOEBound, Vec3 maxAOEBound) implements MSPacket.PlayToClient
{
	public static final Type<MagicAOEEffectPacket> ID = new Type<>(Minestuck.id("magic_aoe_effect"));
	public static final StreamCodec<FriendlyByteBuf, MagicAOEEffectPacket> STREAM_CODEC = StreamCodec.composite(
			NeoForgeStreamCodecs.enumCodec(MagicEffect.AOEType.class),
			MagicAOEEffectPacket::aoeType,
			MSPayloads.VEC3_STREAM_CODEC,
			MagicAOEEffectPacket::minAOEBound,
			MSPayloads.VEC3_STREAM_CODEC,
			MagicAOEEffectPacket::maxAOEBound,
			MagicAOEEffectPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		MagicEffect.AOEParticleEffect(aoeType, Minecraft.getInstance().level, minAOEBound, maxAOEBound);
	}
}