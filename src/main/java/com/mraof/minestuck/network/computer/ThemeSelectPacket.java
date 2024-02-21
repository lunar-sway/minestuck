package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.PlayToServerPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record ThemeSelectPacket(BlockPos pos, ResourceLocation themeId) implements PlayToServerPacket
{
	public static ThemeSelectPacket create(ComputerBlockEntity be, ResourceLocation themeId)
	{
		return new ThemeSelectPacket(be.getBlockPos(), themeId);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeResourceLocation(themeId);
	}
	
	public static ThemeSelectPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		ResourceLocation themeId = buffer.readResourceLocation();
		
		return new ThemeSelectPacket(computer, themeId);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.forNetworkIfPresent(player, pos, computer -> computer.setTheme(themeId));
	}
}
