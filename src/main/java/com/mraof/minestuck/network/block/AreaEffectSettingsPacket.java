package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.AreaEffectBlock;
import com.mraof.minestuck.blockentity.redstone.AreaEffectBlockEntity;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

public record AreaEffectSettingsPacket(Holder<MobEffect> effect, int effectAmp, boolean isAllMobs, BlockPos minEffectPos, BlockPos maxEffectPos, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	
		public static final Type<AreaEffectSettingsPacket> ID = new Type<>(Minestuck.id("area_effect_settings"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AreaEffectSettingsPacket> STREAM_CODEC = StreamCodec.composite(
			MobEffect.STREAM_CODEC,
			AreaEffectSettingsPacket::effect,
			ByteBufCodecs.INT,
			AreaEffectSettingsPacket::effectAmp,
			ByteBufCodecs.BOOL,
			AreaEffectSettingsPacket::isAllMobs,
			BlockPos.STREAM_CODEC,
			AreaEffectSettingsPacket::minEffectPos,
			BlockPos.STREAM_CODEC,
			AreaEffectSettingsPacket::maxEffectPos,
			BlockPos.STREAM_CODEC,
			AreaEffectSettingsPacket::beBlockPos,
			AreaEffectSettingsPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		if(!AreaEffectBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, AreaEffectBlockEntity.class)
				.ifPresent(areaEffect -> areaEffect.handleSettingsPacket(this));
	}
}
