package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ComputerThemes;
import com.mraof.minestuck.network.PlayToServerPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ThemeSelectPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	private final String themeName;
	
	public ThemeSelectPacket(BlockPos pos, String themeName)
	{
		this.pos = pos;
		this.themeName = themeName;
	}
	
	public static ThemeSelectPacket create(ComputerBlockEntity be, String themeName)
	{
		return new ThemeSelectPacket(be.getBlockPos(), themeName);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.forNetworkIfPresent(player, pos, computer -> computer.setTheme(themeName));
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeUtf(themeName, 50);
	}
	
	public static ThemeSelectPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		String themeName = buffer.readUtf(50);
		
		return new ThemeSelectPacket(computer, themeName);
	}
	
}
