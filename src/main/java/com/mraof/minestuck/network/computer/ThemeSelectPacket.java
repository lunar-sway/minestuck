package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ThemeSelectPacket(BlockPos pos, ResourceLocation themeId) implements MSPacket.PlayToServer
{
	public static final Type<ThemeSelectPacket> ID = new Type<>(Minestuck.id("theme_select"));
	public static final StreamCodec<FriendlyByteBuf, ThemeSelectPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			ThemeSelectPacket::pos,
			ResourceLocation.STREAM_CODEC,
			ThemeSelectPacket::themeId,
			ThemeSelectPacket::new
	);
	
	public static ThemeSelectPacket create(ComputerBlockEntity be, ResourceLocation themeId)
	{
		return new ThemeSelectPacket(be.getBlockPos(), themeId);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		ComputerBlockEntity.getAccessibleComputer(player, pos).ifPresent(computer -> computer.setTheme(themeId));
	}
}
