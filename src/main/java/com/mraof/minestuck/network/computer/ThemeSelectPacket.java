package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record ThemeSelectPacket(BlockPos pos, ResourceLocation themeId) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("theme_select");
	
	public static ThemeSelectPacket create(ComputerBlockEntity be, ResourceLocation themeId)
	{
		return new ThemeSelectPacket(be.getBlockPos(), themeId);
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeResourceLocation(themeId);
	}
	
	public static ThemeSelectPacket read(FriendlyByteBuf buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		ResourceLocation themeId = buffer.readResourceLocation();
		
		return new ThemeSelectPacket(computer, themeId);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.getAccessibleComputer(player, pos).ifPresent(computer -> computer.setTheme(themeId));
	}
}
